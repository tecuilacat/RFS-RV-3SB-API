package com.github.tecuilacat.robotapi.api.logger;

import java.time.LocalDateTime;

public class Logger {

    private static boolean ONLY_SHOW_COMMANDS;
    private static final boolean IS_IN_DEBUG_MODE = java.lang.management.ManagementFactory.getRuntimeMXBean()
            .getInputArguments()
            .toString()
            .contains("jdwp");

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";

    private final String name;

    public Logger(Class<?> c) {
        this.name = c.getName();
    }

    public void info(String message) {
        if (!ONLY_SHOW_COMMANDS) {
            System.out.println(ANSI_BLUE + getDateTime() + " --- " + name + "\t\t\t: " + message + ANSI_RESET);
        }
    }

    public void command(String command) {
        if (ONLY_SHOW_COMMANDS || IS_IN_DEBUG_MODE) {
            System.out.println(ANSI_BLUE + getDateTime() + " --- " + name + "\t\t\t: " + command + ANSI_RESET);
        }
    }

    public void debug(String message) {
        if (IS_IN_DEBUG_MODE) {
            System.out.println(ANSI_PURPLE + getDateTime() + " --- " + name + "\t\t\t: " + message + ANSI_RESET);
        }
    }

    public void robotAction(String message) {
        System.out.println(ANSI_GREEN + getDateTime() + " --- " + name + "\t\t\t: " + message + ANSI_RESET);
    }

    public void error(String message) {
        if (!ONLY_SHOW_COMMANDS) {
            System.out.println(ANSI_RED + getDateTime() + " --- " + name + "\t\t\t: " + message + ANSI_RESET);
        }
    }

    public void error(String message, Throwable e) {
        error(message);
        e.printStackTrace();
    }

    private String getDateTime() {
        LocalDateTime date = LocalDateTime.now();
        return date.getYear() + "-" + date.getMonthValue() + "-" + date.getDayOfMonth() + " " + date.getHour() + ":" + date.getMinute() + ":" + date.getSecond();
    }

    /**
     * Only works in debug
     */
    public static void onlyShowCommandsAndDebug() {
        if (IS_IN_DEBUG_MODE) {
            ONLY_SHOW_COMMANDS = true;
        }
    }

}
