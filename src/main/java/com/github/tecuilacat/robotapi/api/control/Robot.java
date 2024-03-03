package com.github.tecuilacat.robotapi.api.control;

import com.github.tecuilacat.robotapi.api.commands.CommandSet;
import com.github.tecuilacat.robotapi.api.logger.Logger;
import com.github.tecuilacat.robotapi.api.nav.Position;
import com.github.tecuilacat.robotapi.api.terminal.Terminal;
import com.github.tecuilacat.robotapi.api.parser.Parser;
import com.github.tecuilacat.robotapi.api.parser.PositionParser;
import com.github.tecuilacat.robotapi.api.parser.RobotStateParser;
import com.github.tecuilacat.robotapi.api.programs.RunnableProgram;
import com.github.tecuilacat.robotapi.api.state.RobotState;
import com.github.tecuilacat.robotapi.api.utils.DelayManager;

import java.net.Socket;
import java.util.Objects;

/**
 * Mitsubishi RV-3SB robot with vacuum pump
 */
public final class Robot implements RobotOperations {

    private static final Logger logger = new Logger(Robot.class);

    private Socket socket;
    private CommandExecutor executor;
    private String name = "Robot";
    private Position safePosition;
    private CommandSet commandSet;

    /**
     * Describes the z-value the robot must always be above in order to not crash into or through the glass surface
     */
    private final double minZvalue;

    private final boolean isMinZvalueValidated;

    Robot(RobotBuilder builder) {
        if (!builder.name.equals("")) {
            this.name = builder.name;
        }
        this.minZvalue = builder.minZvalue;
        this.isMinZvalueValidated = builder.isMinZvalueValidated;
        logger.info("Creating new connection for " + name);
        try {
            socket = new Socket(builder.ipAddress, builder.port);
            executor = CommandExecutor.getFromSocket(socket);
        } catch (Exception e) {
            logger.error("Connection could not be invoked", e);
            if (builder.exitOnError) {
                System.exit(-1); // -1 indicates an error
            }
            return;
        }
        this.commandSet = builder.commandSet;
        logger.info("Configuring Robot " + name);
        if (builder.enableCommunication) {enableCommunication();}
        if (builder.enableOperation) {enableOperation();}
        if (builder.enableServo) {enableServo();}
        setSpeed(builder.speed);

        if (Objects.nonNull(builder.safePosition)) {
            this.safePosition = builder.safePosition;
            movToSafePosition();
        }
        logger.info("Robot is now ready to operate");
        if (builder.enableTerminal) {
            new Terminal(this).open();
        }
    }

    @Override
    public String enableCommunication() {
        logger.info("Enabling communication");
        return executor.execute(commandSet.getEnableCommunicationCommand());
    }

    @Override
    public String disableCommunication() {
        logger.info("Disabling communication");
        return executor.execute(commandSet.getDisableCommunicationCommand());
    }

    @Override
    public String enableOperation() {
        logger.info("Enabling operation");
        return executor.execute(commandSet.getEnableOperationCommand());
    }

    @Override
    public String enableServo() {
        logger.robotAction("Enabling servo");
        String res = executor.execute(commandSet.getServoOnCommand());
        DelayManager.defaultTimeout();
        return res;
    }

    @Override
    public String disableServo() {
        logger.robotAction("Disabling servo");
        String res = executor.execute(commandSet.getServoOffCommand());
        DelayManager.defaultTimeout();
        return res;
    }

    @Override
    public String setSpeed(int speed) {
        logger.robotAction("Setting speed to " + speed);
        String res = executor.execute(commandSet.getSetSpeedCommand() + speed);
        DelayManager.defaultTimeout();
        return res;
    }

    @Override
    public Position getCurrentPosition() {
        String currentPositionAsString = executor.execute(commandSet.getCurrentPositionCommand()).substring(3); //Charactes before that are unneccessary
        return new PositionParser().parse(currentPositionAsString);
    }

    @Override
    public String getState() {
        return executor.execute(commandSet.getStateCommand());
    }

    @Override
    public String movToPositionWithSafeTravel(Position position) {
        if (validatePosition(position)) {
            logger.debug("Moving to position " + position + " via MOV");
            logger.robotAction("Moving");
            String res = executor.execute(commandSet.getMOVCommand() + Position.getDifferenceToPosition(getCurrentPosition(), position.alterZ(-50))); //MOV Psomething -50
            waitForMovementToBeCompleted();
            res += executor.execute(commandSet.getMVSCommand() + Position.getDifferenceToPosition(getCurrentPosition(), position)); //MOV Psomething
            waitForMovementToBeCompleted();
            return res;
        } else {
            return "Aborted moving";
        }
    }

    @Override
    public String movToPosition(Position position) {
        if (validatePosition(position)) {
            logger.debug("Moving to position " + position + " via MOV");
            logger.robotAction("Moving");
            String res = executor.execute(commandSet.getMOVCommand() + Position.getDifferenceToPosition(getCurrentPosition(), position));
            waitForMovementToBeCompleted();
            return res;
        } else {
            return "Aborted moving";
        }
    }

    @Override
    public String mvsToPosition(Position position) {
        logger.debug("Moving to position " + position + " via MVS");
        if (validatePosition(position)) {
            logger.robotAction("Moving");
            String res = executor.execute(commandSet.getMVSCommand() + Position.getDifferenceToPosition(getCurrentPosition(), position)); //MVS Psomething
            waitForMovementToBeCompleted();
            return res;
        } else {
            return "Aborted moving";
        }
    }

    /**
     * Validates the position for security reasons (ensures that the robot arm does not crash into the glass surface
     */
    private boolean validatePosition(Position position) {
        boolean valid = true;
        if (position.getZ() < this.minZvalue && isMinZvalueValidated) {
            valid = false;
            logger.error("Position has a min z-value of " + position.getZ() + ", but the defined minimum is " + this.minZvalue);
        }
        return valid;
    }

    @Override
    public String movToSafePosition() {
        String res = "Keine SafePosition angegeben";
        if (Objects.nonNull(safePosition)) {
            logger.robotAction("Moving to safe Position");
            Position currentPosition = getCurrentPosition();
            if (currentPosition.getZ() != safePosition.getZ()) {
                res = mvsToPosition(currentPosition.alterAbsoluteZ(safePosition.getZ()));
                waitForMovementToBeCompleted();
            }
            res += movToPositionWithSafeTravel(safePosition);
            waitForMovementToBeCompleted();
        } else {
            logger.error("Keine SafePosition angegeben!");
        }
        return res;
    }

    @Override
    public String executeCustomCommand(String cmd) {
        return executor.execute(cmd);
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
        executor.execute(commandSet.getGrabCommand());
        DelayManager.defaultTimeout();
    }

    @Override
    public void drop() {
        logger.robotAction("Dropping");
        executor.execute(commandSet.getDropCommand());
        DelayManager.defaultTimeout();
    }

    @Override
    public void runProgram(RunnableProgram runnableProgram) {
        runnableProgram.runProgram(this);
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
    public void delay(double seconds) {
        DelayManager.sleep(seconds);
    }

    @Override
    public void destroy() {
        drop();
        movToSafePosition();
        disconnect();
    }

    @Override
    public void notifyNcMachine() {
        logger.info("Notifying NC-Machine");
        executor.execute(commandSet.getNotifyNcMachine());
        DelayManager.sleep(2);
        executor.execute(commandSet.getResetNcMachine());
    }

    public String getName() {
        return name;
    }
}
