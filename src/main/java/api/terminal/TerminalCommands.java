package api.terminal;

import api.control.RobotOperations;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.function.Function;

/**
 * Enum holds all possible Terminal or Console commands
 */
public enum TerminalCommands {

    GRAB("grab", "turns pump on", TerminalOperations::grab, ""),
    DROP("drop", "turns pump off", TerminalOperations::drop, ""),
    SERVO_ON("servo on", "turns servo on", TerminalOperations::enableServo, ""),
    SERVO_OFF("servo off", "turns servo off", TerminalOperations::disableServo, ""),
    CURRENT_POSITION("curr", "returns current position", TerminalOperations::getCurrentPosition, ""),
    STATE("state", "gets state of robot", TerminalOperations::getState, ""),
    SET_SPEED("speed", "sets speed of robot", TerminalOperations::setSpeed, "speed\\s\\d+"),
    HELP("help", "prints help", TerminalOperations::help, ""),
    QUIT("quit", "quits the program", robotOperations -> null, ""), //is handled outside this class
    MOVX("movx", "mov in x-direction", TerminalOperations::moveRobot, "movx\\s-?\\d+(\\.\\d+)?"),
    MOVY("movy", "mov in y-direction", TerminalOperations::moveRobot, "movy\\s-?\\d+(\\.\\d+)?"),
    MOVZ("movz", "mov in z-direction", TerminalOperations::moveRobot, "movz\\s-?\\d+(\\.\\d+)?"),
    MVSX("mvsx", "mvs in x-direction", TerminalOperations::moveRobot, "mvsx\\s-?\\d+(\\.\\d+)?"),
    MVSY("mvsy", "mvs in y-direction", TerminalOperations::moveRobot, "mvsy\\s-?\\d+(\\.\\d+)?"),
    MVSZ("mvsz", "mvs in z-direction", TerminalOperations::moveRobot, "mvsz\\s-?\\d+(\\.\\d+)?"),
    MOV_SAFE("mov safe", "moves to safe position", TerminalOperations::movToSafePosition, "")
    ;

    private final String command;
    private final String info;
    private final Function<Params, String> function;
    private final String pattern;

    TerminalCommands(String command, String info, Function<Params, String> function, String pattern) {
        this.command = command;
        this.info = info;
        this.function = function;
        this.pattern = pattern;
    }

    public boolean isValid(String cmd) {
        String regex;
        if (StringUtils.isEmpty(pattern)) {
            regex = "^" + command + "$";
        } else {
            regex = pattern;
        }
        return cmd.toLowerCase().matches(regex.toLowerCase());
    }

    public String getCommand() {
        return command;
    }

    public String getInfo() {
        return info;
    }

    public String execute(RobotOperations ops, String cmd) {
        return function.apply(new Params(this, ops,cmd.toUpperCase().replaceAll("\\.", ".")));
    }

    public String getPattern() {
        return pattern;
    }

    public static TerminalCommands getCommand(final String command) {
        return Arrays.stream(TerminalCommands.values())
                .filter(cmd -> cmd.isValid(command))
                .findFirst()
                .orElseThrow(() -> new EnumConstantNotPresentException(TerminalCommands.class, command));
    }
}
