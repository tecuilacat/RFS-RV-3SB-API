package api.control;

import api.commands.Commands;
import api.logger.Logger;
import api.nav.Position;
import api.parser.Parser;
import api.parser.PositionParser;
import api.parser.RobotStateParser;
import api.programs.RunnableProgram;
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
    private CommandExecutor executor;
    private String name = "";
    private Position safePosition;

    protected RV3SB(RobotBuilder builder) {
        if (!builder.name.equals("")) this.name = builder.name;
        logger.info("Creating new connection for " + name);
        try {
            socket = new Socket(builder.ipAddress, builder.port);
            executor = new CommandExecutor(socket.getInputStream(), socket.getOutputStream());
        } catch (Exception e) {
            logger.error("Connection could not be invoked", e);
            if (builder.exitOnError) System.exit(-1); // -1 indicates an error
            return;
        }
        logger.info("Configuring Robot " + name);
        if (builder.enableCommunication) enableCommunication();
        if (builder.enableOperation) enableOperation();
        if (builder.enableServo) enableServo();
        setSpeed(builder.speed);

        if (Objects.nonNull(builder.safePosition)) {
            this.safePosition = builder.safePosition;
            movToSafePosition();
        }
        logger.info("Robot is now ready to operate");
    }

    @Override
    public String enableCommunication() {
        logger.info("Enabling communication");
        return executor.execute(Commands.ENABLE_COMMUNICATION.getCommand());
    }

    @Override
    public String disableCommunication() {
        logger.info("Disabling communication");
        return executor.execute(Commands.DISABLE_COMMUNICATION.getCommand());
    }

    @Override
    public String enableOperation() {
        logger.info("Enabling operation");
        return executor.execute(Commands.ENABLE_OPERATION.getCommand());
    }

    @Override
    public String enableServo() {
        logger.robotAction("Enabling servo");
        String res = executor.execute(Commands.SERVO_ON.getCommand());
        DelayManager.defaultTimeout();
        return res;
    }

    @Override
    public String disableServo() {
        logger.robotAction("Disabling servo");
        String res = executor.execute(Commands.SERVO_OFF.getCommand());
        DelayManager.defaultTimeout();
        return res;
    }

    @Override
    public String setSpeed(int speed) {
        logger.robotAction("Setting speed to " + speed);
        String res = executor.execute(Commands.SET_SPEED.getCommand() + speed);
        DelayManager.defaultTimeout();
        return res;
    }

    @Override
    public Position getCurrentPosition() {
        String currentPositionAsString = executor.execute(Commands.GET_CURRENT_POSITION.getCommand()).substring(3); //Charactes before that are unneccessary
        return new PositionParser().parse(currentPositionAsString);
    }

    @Override
    public String getState() {
        return executor.execute(Commands.STATE.getCommand());
    }

    @Override
    public String movToPosition(Position position, boolean withSafeTravel) {
        logger.debug("Moving to position " + position + " via MOV");
        logger.robotAction("Moving");
        String res;
        if (withSafeTravel) { //moves to the safe z index first
            res = executor.execute(Commands.MOV.getCommand() + Position.getDifferenceToPosition(getCurrentPosition(), position.alterZ(-50))); //MOV Psomething -50
            waitForMovementToBeCompleted();
            res += executor.execute(Commands.MVS.getCommand() + Position.getDifferenceToPosition(getCurrentPosition(), position)); //MOV Psomething
        } else {
            res = executor.execute(Commands.MOV.getCommand() + Position.getDifferenceToPosition(getCurrentPosition(), position)); //MOV Psomething
        }
        waitForMovementToBeCompleted();
        return res;
    }

    @Override
    public String movToPosition(Position position) {
        logger.debug("Moving to position " + position + " via MOV");
        logger.robotAction("Moving");
        String res = executor.execute(Commands.MOV.getCommand() + Position.getDifferenceToPosition(getCurrentPosition(), position));
        waitForMovementToBeCompleted();
        return res;
    }

    @Override
    public String mvsToPosition(Position position) {
        logger.debug("Moving to position " + position + " via MVS");
        logger.robotAction("Moving");
        String res = executor.execute(Commands.MVS.getCommand() + Position.getDifferenceToPosition(getCurrentPosition(), position)); //MVS Psomething
        waitForMovementToBeCompleted();
        return res;
    }

    @Override
    public String movToSafePosition() {
        String res = "Keine SafePosition angegeben";
        if (Objects.nonNull(safePosition)) {
            logger.robotAction("Moving to save Position");
            Position currentPosition = getCurrentPosition();
            if (currentPosition.getZ() != safePosition.getZ()) {
                res = mvsToPosition(currentPosition.alterAbsoluteZ(safePosition.getZ()));
                waitForMovementToBeCompleted();
            }
            res += movToPosition(safePosition, false);
            waitForMovementToBeCompleted();
        } else {
            logger.error("Keine SafePosition angegeben!");
        }
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
        executor.execute(Commands.GRAB.getCommand());
        DelayManager.defaultTimeout();
    }

    @Override
    public void drop() {
        logger.robotAction("Dropping");
        executor.execute(Commands.DROP.getCommand());
        DelayManager.defaultTimeout();
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

    @Override
    public void runProgram(RunnableProgram runnableProgram) {
        runnableProgram.runProgram(this);
    }

}
