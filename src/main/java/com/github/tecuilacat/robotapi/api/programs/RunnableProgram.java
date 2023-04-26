package com.github.tecuilacat.robotapi.api.programs;

import com.github.tecuilacat.robotapi.api.control.RobotOperations;

/**
 * Implement a program based on a controller or robot here
 * Example: Subprogram to grab to a position which is easily callable from another program
 */
public interface RunnableProgram {

    /**
     * Runs a program on a robot
     * @param robot Controller that controls the program operations
     */
    void runProgram(RobotOperations robot);

}
