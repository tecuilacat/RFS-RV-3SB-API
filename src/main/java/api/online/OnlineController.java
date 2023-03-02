package api.online;

import api.commands.CommandSet;
import api.control.Robot;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Allows a live control over the robot. Enter raw commands into the console and control everything manually
 *
 * TODO ? | OS | 25.02.2023 | Klasse testen und schauen, ob praktikabel. Wenn nicht, rausyeeten!
 */
public class OnlineController {

    private final List<String> commands = new ArrayList<>();

    private final Robot robot;

    private static final String QUIT_COMMAND = "quit";
    private static final String HELP_COMMAND = "help";

    public OnlineController(Robot robotOperations, CommandSet commandSet) {
        this.robot = robotOperations;
        Method[] methods = commandSet.getClass().getDeclaredMethods();
        for (Method method: methods) {
            try {
                if (method.getReturnType().equals(String.class)) {
                    String res = (String) method.invoke(commandSet);
                    commands.add(res);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void start() {
        System.out.println("Online control active. You´re live.");
        printHelp();
        System.out.println(" ");

        Scanner scanner = new Scanner(System.in);

        boolean keepRunning = true;
        while (keepRunning) {
//            System.out.print(robot.getName() + ">");
            System.out.print("RV-3SB>");
            String command = scanner.nextLine().toUpperCase();
            if (isValidCommand(command)) {
                robot.executeCustomCommand(command);
            } else if (StringUtils.equalsIgnoreCase(command, QUIT_COMMAND)) {
                keepRunning = false;
            } else if (StringUtils.equalsIgnoreCase(command, HELP_COMMAND)) {
                printHelp();
            } else {
                System.out.println("Command '" + command + "' not found.");
            }
        }
        System.out.println("Online control shut down");
        robot.drop();
        robot.disableServo();
        System.exit(0);
    }

    private boolean isValidCommand(String command) {
        return commands.stream()
                .anyMatch(c -> StringUtils.startsWithIgnoreCase(command, c));
    }

    private void printHelp() {
        System.out.println("Following commands are available:");
        commands.forEach(c -> System.out.println("\t- " + c));
        System.out.println("Enter commands (to quit type '" + QUIT_COMMAND + "' and press enter | if you need Help, type '" + HELP_COMMAND + "' and press enter)");
    }

}
