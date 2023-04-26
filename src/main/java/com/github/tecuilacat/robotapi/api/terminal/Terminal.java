package com.github.tecuilacat.robotapi.api.terminal;

import com.github.tecuilacat.robotapi.api.control.Robot;

import java.util.Scanner;

/**
 * Class allows user to operate the roboter in the commandLine of either a console or inside the IDE
 */
public class Terminal {

    private final Robot robot;

    public Terminal(Robot robot) {
        this.robot = robot;
    }

    /**
     * Activates the operation via terminal or console
     */
    public void open() {
        Scanner scanner = new Scanner(System.in);
        boolean keepRunning = true;
        while (keepRunning) {
            System.out.print(robot.getName() + ">");
            String command = scanner.nextLine();
            if (command.equalsIgnoreCase(TerminalCommands.QUIT.getCommand())) {
                keepRunning = false;
            } else if (command.isBlank()) {
                try {
                    TerminalCommands cmd = TerminalCommands.getCommand(command);
                    String res = cmd.execute(robot, command);
                    if (!res.isEmpty()) System.out.println(res);
                } catch (Exception e) {
                    System.out.println(command + " not found");
                }
            }
        }
        robot.drop();
        robot.disableServo();
        scanner.close();
    }

}
