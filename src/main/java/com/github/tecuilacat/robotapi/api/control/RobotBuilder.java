package com.github.tecuilacat.robotapi.api.control;

import com.github.tecuilacat.robotapi.api.commands.CommandSet;
import com.github.tecuilacat.robotapi.api.commands.MelfaBasic4CommandSet;
import com.github.tecuilacat.robotapi.api.logger.Logger;
import com.github.tecuilacat.robotapi.api.nav.Position;

import java.util.Objects;

/**
 * Creates a robot and starts it up in a secure way
 * Builder ensures the correct procedure of starting up (first communication, then operation, then servo, then speed, then the rest)
 */
public class RobotBuilder {

    private static final Logger logger = new Logger(RobotBuilder.class);

    final String ipAddress;
    final int port;
    boolean enableCommunication;
    boolean enableOperation;
    boolean enableServo;
    boolean exitOnError;
    boolean enableTerminal;
    int speed = 10;
    Position safePosition;
    String name = "robot";
    CommandSet commandSet;

    private boolean disableSecureStartup = false;

    /**
     * Creates the first steps for the connection to a robot
     * @param ipAddress Ip-Address of the robot
     * @param port Port of the robot
     */
    public RobotBuilder(String ipAddress, int port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    /**
     * Builds the connection to the robot and initializes dedicated variables / operations
     * @return Robot ready with the configured attributes
     */
    public Robot build() {
        if (Objects.isNull(safePosition) && !disableSecureStartup) {
            logger.error("You must define a safe position for your robot!");
            return null;
        }
        if (Objects.isNull(commandSet)) {
            logger.error("You must define a command set for the robot");
            return null;
        }
        try {
            return new Robot(this);
        } catch (Exception e) {
            logger.error("Robot could not be initialized", e);
        }
        return null;
    }

    /**
     * Builds a robot with the preconfigured attributes: enableOperation, enableCommunication, enableServo, exitOnError, setSpeed = 10
     * @param safePosition Safe position of the robot (can be null if not configure [not recommended])
     * @return Robot ready to work
     */
    public Robot buildPreConfig(Position safePosition) {
        this.setSafePosition(safePosition);
        this.enableOperation();
        this.enableCommunication();
        this.enableServo();
        this.exitOnError();
        this.commandSet = MelfaBasic4CommandSet.getCommandSet();
        if (Objects.isNull(safePosition)) {
            this.disableSecureStartup();
        }
        return build();
    }

    /**
     * Enables the communication between your computer and the robot after startup
     * @return Instance of RobotBuilder
     */
    public RobotBuilder enableCommunication() {
        this.enableCommunication = true;
        return this;
    }

    /**
     * Enables operation of the robot after startup
     * @return Instance of RobotBuilder
     */
    public RobotBuilder enableOperation() {
        this.enableOperation = true;
        return this;
    }

    /**
     * Enables the servo of the robot after startup
     * @return Instance of RobotBuilder
     */
    public RobotBuilder enableServo() {
        this.enableServo = true;
        return this;
    }

    /**
     * Sets the speed of the robot (0 < speed <= 100)
     * @param speed Moving speed of the robot
     * @return Instance of RobotBuilder
     */
    public RobotBuilder setSpeed(int speed) {
        if (speed <= 0) {
            this.speed = 1;
        } else {
            this.speed = Math.min(speed, 100);
        }
        return this;
    }

    /**
     * Sets the safe position of the robot
     * @param position Safe position
     * @return Instance of RobotBuilder
     */
    public RobotBuilder setSafePosition(Position position) {
        this.safePosition = position;
        return this;
    }

    /**
     * Sets the name of the robot (you do not necessarily need this)
     * @param name Name of the robot
     * @return Instance of RobotBuilder
     */
    public RobotBuilder setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Disables a secure startup meaning the robot will not be able to move to a safe position
     *
     * Usually the robot needs a safe position so you can easily access that operation by one method call. This method will disable that operation
     * @return Instance of RobotBuilder
     */
    public RobotBuilder disableSecureStartup() {
        this.disableSecureStartup = true;
        return this;
    }

    /**
     * Stops the application if there occurs an error connecting the robot with an error code (-1)
     * @return Instance of RobotBuilder
     */
    public RobotBuilder exitOnError() {
        this.exitOnError = true;
        return this;
    }

    /**
     * Commandset for the robot
     * @param commandSet For example 'MelfaBasic4CommandSet'
     * @return Instance of RobotBuilder
     */
    public RobotBuilder setCommandSet(CommandSet commandSet) {
        this.commandSet = commandSet;
        return this;
    }

    /**
     * Enables the operation of the roboter via the console
     * @return Instance of RobotBuilder
     */
    public RobotBuilder enableTerminalOperation() {
        this.enableTerminal = true;
        return this;
    }

}
