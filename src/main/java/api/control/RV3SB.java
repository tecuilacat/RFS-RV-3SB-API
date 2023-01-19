package api.control;

import api.commands.Commands;
import api.logger.Logger;
import api.nav.Position;
import api.parser.Parser;
import api.parser.RobotStateParser;
import api.state.RobotState;
import api.utils.DelayManager;

import java.net.Socket;
import java.util.Objects;

/**
 * Mitsubishi RV-3SB robot with vacuum pump
 */
public class RV3SB implements RobotOperations {

    private static final Logger logger = new Logger(RV3SB.class);
    private static final double SAFE_Z = 300.0;

    private Socket socket;
    private CommandExecuter executer;
    private String name = "";
    private Position safePosition;

    protected RV3SB(RobotBuilder builder) {
        if (!builder.name.equals("")) this.name = builder.name;
        logger.info("Creating new connection for " + name);
        try {
            socket = new Socket(builder.ipAddress, builder.port);
            executer = new CommandExecuter(socket.getInputStream(), socket.getOutputStream());
        } catch (Exception e) {
            logger.error("Connection could not be invoked", e);
            if (builder.exitOnError) System.exit(-1);
            return;
        }
        logger.info("Configuring Robot " + name);
        if (builder.enableCommunication) enableCommunication();
        if (builder.enableOperation) enableOperation();
        if (builder.enableServo) enableServo();
        setSpeed(builder.speed);

        if (Objects.nonNull(builder.safePosition)) {
            this.safePosition = builder.safePosition;
            movToPosition(safePosition, true); //moves up and then to the safe position
        }
        logger.info("Robot is now ready to operate");
    }

    @Override
    public String enableCommunication() {
        logger.info("Enabling communication");
        return executer.execute(Commands.ENABLE_COMMUNICATION.getCommand());
    }

    @Override
    public String disableCommunication() {
        logger.info("Disabling communication");
        return executer.execute(Commands.DISABLE_COMMUNICATION.getCommand());
    }

    @Override
    public String enableOperation() {
        logger.info("Enabling operation");
        return executer.execute(Commands.ENABLE_OPERATION.getCommand());
    }

    @Override
    public String enableServo() {
        logger.robotAction("Enabling servo");
        String res = executer.execute(Commands.SERVO_ON.getCommand());
        DelayManager.defaultTimeout();
        return res;
    }

    @Override
    public String disableServo() {
        logger.robotAction("Disabling servo");
        String res = executer.execute(Commands.SERVO_OFF.getCommand());
        DelayManager.defaultTimeout();
        return res;
    }

    @Override
    public String setSpeed(int speed) {
        logger.robotAction("Setting speed to " + speed);
        String res = executer.execute(Commands.SET_SPEED.getCommand() + speed);
        DelayManager.defaultTimeout();
        return res;
    }

    @Override
    public Position getCurrentPosition() {
        String answer = executer.execute(Commands.GET_CURRENT_POSITION.getCommand()).substring(3);
        String[] vals = answer.split(";"); //for parsing
        return new Position(
                Double.parseDouble(vals[1]),
                Double.parseDouble(vals[3]),
                Double.parseDouble(vals[5]),
                Double.parseDouble(vals[7]),
                Double.parseDouble(vals[9]),
                Double.parseDouble(vals[11])
        );
    }

    @Override
    public String getState() {
        return executer.execute(Commands.STATE.getCommand());
    }

    @Override
    public String movToPosition(Position position, boolean withSafeTravel) {
        logger.debug("Moving to position " + position + " via MOV");
        logger.robotAction("Moving");
        String res;
        if (withSafeTravel) { //moves to the safe z index first
            Position tmp = position.copy();
            tmp.setZ(position.getZ() + 50);
            res = executer.execute(Commands.MOV.getCommand() + getDifferenceToPosition(tmp)); //MOV Psomething -50
            waitForMovementToBeCompleted();
            res += executer.execute(Commands.MVS.getCommand() + getDifferenceToPosition(position));
        } else {
            res = executer.execute(Commands.MOV.getCommand() + getDifferenceToPosition(position));
        }
        waitForMovementToBeCompleted();
        return res;
    }

    @Override
    public String mvsToPosition(Position position) {
        logger.debug("Moving to position " + position + " via MVS");
        logger.robotAction("Moving");
        String res = executer.execute(Commands.MVS.getCommand() + getDifferenceToPosition(getDifferenceToPosition(position)));
        waitForMovementToBeCompleted();
        return res;
    }

    @Override
    public String movToSafePosition() {
        String res = "Keine SafePosition angegeben";
        if (Objects.nonNull(safePosition)) {
            logger.robotAction("Moving to save Position");
            if (getCurrentPosition().getZ() < safePosition.getZ()) {
                Position tmp = getCurrentPosition();
                tmp.setZ(SAFE_Z);
                res = mvsToPosition(tmp);
                waitForMovementToBeCompleted();
            }
            res += movToPosition(safePosition, false);
        }
        waitForMovementToBeCompleted();
        return res;
    }

    @Override
    public void disconnect() {
        logger.info("Disconnecting");
        try {
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isMoving() {
        Parser<String, RobotState> robotStateParser = new RobotStateParser();
        RobotState robotState = robotStateParser.parse(getState());
        logger.debug(robotState.isMoving()? "Robot is moving": "Robot stopped moving");
        return robotState.isMoving();
    }

    @Override
    public void setSafePosition(Position safePosition) {
        this.safePosition = safePosition;
    }

    @Override
    public void grab() {
        logger.robotAction("Grabbing");
        executer.execute(Commands.GRAB.getCommand());
        DelayManager.defaultTimeout();
    }

    @Override
    public void drop() {
        logger.robotAction("Dropping");
        executer.execute(Commands.DROP.getCommand());
        DelayManager.defaultTimeout();
    }

    /**
     * Calculates the relative position to the current position which you need to move the robot to a certain position
     *
     * @param position Position you want to go to
     * @return Relative position to the current position
     */
    private Position getDifferenceToPosition(Position position) {
        Position currentPosition = getCurrentPosition();
        return new Position(
                position.getX() - currentPosition.getX(),
                position.getY() - currentPosition.getY(),
                position.getZ() - currentPosition.getZ(),
                position.getA() - currentPosition.getA(),
                position.getB() - currentPosition.getB(),
                position.getC() - currentPosition.getC()
        );
    }

    /**
     * Pauses the thread until the current motion of the robot has ended
     */
    private void waitForMovementToBeCompleted() {
        DelayManager.defaultTimeout();
        while (isMoving()) {
            DelayManager.defaultTimeout();
        }
    }

}
