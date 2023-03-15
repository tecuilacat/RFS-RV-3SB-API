package api.commands;

/**
 * Interface holds methods to retrieve all commands in a certain language or set of commands
 */
public interface CommandSet {

    /**
     * @return Command to turn on the servo of the robot
     */
    String getServoOnCommand();

    /**
     * @return Command to turn off the servo of the robot
     */
    String getServoOffCommand();

    /**
     * @return Command to enable the communication between the robot and your PC
     */
    String getEnableCommunicationCommand();

    /**
     * @return Command to disable the communication between the robot and your PC
     */
    String getDisableCommunicationCommand();

    /**
     * @return Command to enable the operation on the robot
     */
    String getEnableOperationCommand();

    /**
     * @return Command to disable the operation on the robot
     */
    @Deprecated
    String getDisableOperationCommand();

    /**
     * @return Command to set the robots speed
     */
    String getSetSpeedCommand();

    /**
     * @return Command to move the robot via MOV-command
     */
    String getMOVCommand();

    /**
     * @return Command to move the robot via MVS command
     */
    String getMVSCommand();

    /**
     * @return Command to get the state of the robot
     */
    String getStateCommand();

    /**
     * @return Command to get the position the robot is currently at
     */
    String getCurrentPositionCommand();

    /**
     * @return Command to grab / turn on the pump
     */
    String getGrabCommand();

    /**
     * @return Command to drop / turn off the pump
     */
    String getDropCommand();

    /**
     * @return Command to notify NC-Machine
     */
    String getNotifyNcMachine();

    /**
     * @return Command to reset notification for NC-Machine
     */
    String getResetNcMachine();

}
