package api.online;

import api.control.RobotOperations;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TerminalOperations {

    public static String enableServo(Params params) {
        return params.getOps().enableServo();
    }

    public static String disableServo(Params params) {
        return params.getOps().disableServo();
    }

    public static String setSpeed(Params params) {
        return "override speed";
    }

    public static String getCurrentPosition(Params params) {
        return params.getOps().getCurrentPosition().toString();
    }

    public static String getState(Params params) {
        return params.getOps().getState();
    }

    public static String movToSafePosition(Params params) {
        return params.getOps().movToSafePosition();
    }

    public static String executeCustomCommand(Params params) {
        return params.getOps().executeCustomCommand(params.getCmd());
    }

    public static String grab(Params params) {
        RobotOperations ops = params.getOps();
        ops.grab();
        return "";
    }

    public static String drop(Params params) {
        RobotOperations ops = params.getOps();
        ops.drop();
        return "";
    }

    public static String moveRobot(Params params) {
        String result;
        String parsedCoordinate = parseSingleCoordinate(params.getCmd());
        if (StringUtils.isNotBlank(parsedCoordinate)) {
            switch (params.getTerminalCommand()) {
                case MOVX:
                    params.getOps().executeCustomCommand("EXECMOV P_CURR+("  + parsedCoordinate + ",0.0,0.0,0.0,0.0,0.0)");
                    result = "MOV " + parsedCoordinate + " on X-Axis";
                    break;
                case MOVY:
                    params.getOps().executeCustomCommand("EXECMOV P_CURR+(0.0,"  + parsedCoordinate + ",0.0,0.0,0.0,0.0)");
                    result = "MOV " + parsedCoordinate + " on Y-Axis";
                    break;
                case MOVZ:
                    params.getOps().executeCustomCommand("EXECMOV P_CURR+(0.0,0.0,"  + parsedCoordinate + ",0.0,0.0,0.0)");
                    result = "MOV " + parsedCoordinate + " on Z-Axis";
                    break;
                case MVSX:
                    params.getOps().executeCustomCommand("EXECMVS P_CURR+("  + parsedCoordinate + ",0.0,0.0,0.0,0.0,0.0)");
                    result = "MVS " + parsedCoordinate + " on X-Axis";
                    break;
                case MVSY:
                    params.getOps().executeCustomCommand("EXECMVS P_CURR+(0.0,"  + parsedCoordinate + ",0.0,0.0,0.0,0.0)");
                    result = "MVS " + parsedCoordinate + " on Y-Axis";
                    break;
                case MVSZ:
                    params.getOps().executeCustomCommand("EXECMVS P_CURR+(0.0,0.0,"  + parsedCoordinate + ",0.0,0.0,0.0)");
                    result = "MVS " + parsedCoordinate + " on Z-Axis";
                    break;
                default:
                    result = "Fatal error";
                    break;
            }
        } else {
            result = "Syntax error in: " + params.getCmd() + " (" + params.getTerminalCommand().getPattern() + ")";
        }
        return result;
    }

    private static String parseSingleCoordinate(String cmd) {
        Pattern pattern = Pattern.compile("-?\\d+\\.\\d+");
        Matcher matcher = pattern.matcher(cmd);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return "";
        }
    }

    public static String help(Params params) {
        System.out.println("Available commands:");
        Arrays.stream(TerminalCommands.values()).forEach(command -> System.out.format("%15s|%30s|%20s", command.getCommand() + " ", command.getInfo() + " ", " " + command.getPattern() + "\n"));
        return "";
    }
}
