package api.online;

import api.control.RobotOperations;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TerminalOperations {

    public static String enableServo(Params params) {
        return "";
    }

    public static String disableServo(Params params) {
        return "";
    }

    public static String setSpeed(Params params) {
        return "";
    }

    public static String getCurrentPosition(Params params) {
        return "";
    }

    public static String getState(Params params) {
        return "";
    }

    public static String movToSafePosition(Params params) {
        return "";
    }

    public static String executeCustomCommand(String cmd) {
        return "";
    }

    public static String grab(Params params) {
        RobotOperations ops = params.getOps();
//        ops.grab();
        return "";
    }

    public static String drop(Params params) {
        RobotOperations ops = params.getOps();
//        ops.drop();
        return "";
    }

    public static String movX(Params params) {
        String command = params.getCmd();
        return "EXECMOV P_CURR+("  + parseSingleCoordinate(command) + ",0.0,0.0,0.0,0.0,0.0)";
    }

    public static String movY(Params params) {
        String command = params.getCmd();
        return "EXECMOV P_CURR+(0.0,"  + parseSingleCoordinate(command) + ",0.0,0.0,0.0,0.0)";
    }

    public static String movZ(Params params) {
        String command = params.getCmd();
        return "EXECMOV P_CURR+(0.0,0.0,"  + parseSingleCoordinate(command) + ",0.0,0.0,0.0)";
    }

    public static String mvsX(Params params) {
        String command = params.getCmd();
        return "EXECMVS P_CURR+(" + parseSingleCoordinate(command) + ",0.0,0.0,0.0,0.0,0.0)";
    }

    public static String mvsY(Params params) {
        String command = params.getCmd();
        return "EXECMVS P_CURR+(0.0,"  + parseSingleCoordinate(command) + ",0.0,0.0,0.0,0.0)";
    }

    public static String mvsZ(Params params) {
        String command = params.getCmd();
        return "EXECMVS P_CURR+(0.0,0.0,"  + parseSingleCoordinate(command) + ",0.0,0.0,0.0)";
    }

    private static String parseSingleCoordinate(String cmd) {
        Pattern pattern = Pattern.compile("-?\\d+\\.\\d+");
        Matcher matcher = pattern.matcher(cmd);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return "0.0";
        }
    }

    public static String help(Params params) {
        System.out.println("Available commands:");
        Arrays.stream(TerminalCommands.values()).forEach(command -> System.out.format("%15s|%30s|%20s", command.getCommand() + " ", command.getInfo() + " ", " " + command.getPattern() + "\n"));
        return "";
    }
}
