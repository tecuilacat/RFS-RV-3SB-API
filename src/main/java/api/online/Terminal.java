package api.online;

import api.control.Robot;
import org.apache.commons.lang3.StringUtils;

import java.util.Scanner;

public class Terminal {

    private final Robot robot;

    public Terminal(Robot robot) {
        this.robot = robot;
    }

    public void open() {
        Scanner scanner = new Scanner(System.in);
        boolean keepRunning = true;
        while (keepRunning) {
            System.out.print(robot.getName() + ">");
            String command = scanner.nextLine();
            if (StringUtils.equalsIgnoreCase(command, TerminalCommands.QUIT.getCommand())) {
                keepRunning = false;
            } else if (StringUtils.isNotBlank(command)) {
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
