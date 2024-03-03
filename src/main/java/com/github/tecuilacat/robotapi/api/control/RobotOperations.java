package com.github.tecuilacat.robotapi.api.control;

import com.github.tecuilacat.robotapi.api.nav.Position;
import com.github.tecuilacat.robotapi.api.programs.RunnableProgram;

/**
 * Interface holds all operations that are possible
 */
public interface RobotOperations {

    /**
     * Enables the communication between the computer and the robot
     * @return Answer of the robot
     */
    String enableCommunication();

    /**
     * Disables the communication between the computer and the robot
     * @return Answer of the robot
     */
    String disableCommunication();

    /**
     * Enables the operation of the robot
     * @return Answer of the robot
     */
    String enableOperation();

    /**
     * Turns the servo of the robot on
     * @return Answer of the robot
     */
    String enableServo();

    /**
     * Turns the servo of the robot off
     * @return Answer of the robot
     */
    String disableServo();

    /**
     * Sets the speed in which the robot operates
     * @param speed Value between 0 and 100 (inclusive): less than 0 will result in 0 and more than 100 in 100
     * @return Answer of the robot
     */
    String setSpeed(int speed);

    /**
     * Gets the current position of the robot
     * @return Current Position of the robot
     */
    Position getCurrentPosition();

    /**
     * Gets the current operating state of the robot
     * @return Current state of the robot
     */
    String getState();

    /**
     * Performs a MOV-command to a new position
     * * First it performs a mov-command to the point with point.z = point.z+50 and then a mvs command to the actual point
     * @param position New position
     * @return Answer of the robot
     */
    String movToPositionWithSafeTravel(Position position);

    /**
     * Performs a MOV-command to a new position
     *
     * @param position New position
     * @return Answer of the robot
     */
    String movToPosition(Position position);

    /**
     * Performs a MVS-command to a new position
     * @param position New position
     * @return Answer of the robot
     */
    String mvsToPosition(Position position);

    /**
     * Moves the robot to the dedicated safe position
     * @return Answer of the robot
     */
    String movToSafePosition();

    /**
     * Executes a custom command you can enter as a string (try to only use than as debug tool)
     * @param cmd Command
     * @return Answer of the robot
     */
    String executeCustomCommand(String cmd);

    /**
     * Sets the safe position of the robot
     * @param safePosition New safe position
     */
    void setSafePosition(Position safePosition);

    /**
     * Activates the pump
     */
    void grab();

    /**
     * Deactivates the pump
     */
    void drop();

    /**
     * Closes the connection to the robot
     */
    void disconnect();

    /**
     * In case of an error, the response will be true to ensure, that no other commands are sent to the robot
     * @return Robot is still in motion or not
     */
    boolean isMoving();

    /**
     * Runs a program
     *
     * @param runnableProgram Implementation of a runnable Program
     */
    void runProgram(RunnableProgram runnableProgram);

    /**
     * Interrupts the robot thread for a certain duration
     * @param seconds Duration in seconds
     */
    void delay(double seconds);

    /**
     * Closes the connection to the robot, moves it to the safelocation and disables servo
     */
    void destroy();

    /**
     * Sends notification to the NC-machine
     */
    void notifyNcMachine();

    void versionTesting();

}