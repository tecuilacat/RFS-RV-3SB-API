package api.control;

import api.logger.Logger;
import api.nav.Position;

import java.util.Objects;

/**
 * Creates a robot and starts it up in a secure way
 * Builder ensures the correct procedure of starting up (first communication, then operation, then servo, then speed, then the rest)
 */
public class RobotBuilder {

    private static final Logger logger = new Logger(RobotBuilder.class);

    protected final String ipAddress;
    protected final int port;
    protected boolean enableCommunication = false;
    protected boolean enableOperation = false;
    protected boolean enableServo = false;
    protected int speed = 10;
    protected Position safePosition = null;
    protected String name = "robot";
    protected boolean exitOnError = false;

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
     * @return Robot ready to work with
     */
    public RV3SB build() {
        if (Objects.isNull(safePosition) && !disableSecureStartup) {
            logger.error("You must define a safe position for your robot!");
            return null;
        }
        try {
            return new RV3SB(this);
        } catch (Exception e) {
            logger.error("Robot could not be initialized", e);
        }
        return null;
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

}
