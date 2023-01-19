package api.commands;

/**
 * Enum holds all needed commands for language MELFA Basic 4
 */
public enum Commands {

    SERVO_ON("SRVON"),
    SERVO_OFF("SRVOFF"),
    ENABLE_COMMUNICATION("OPEN=NARCUSR"),
    DISABLE_COMMUNICATION("CLOSE"),
    ENABLE_OPERATION("CNTLON"),
    SET_SPEED("OVRD="),
    MOV("EXECMOV P_CURR+"),
    MVS("EXECMVS P_CURR+"),
    STATE("STATE"),
    GET_CURRENT_POSITION("PPOSF"),
    GRAB("OUT=5;1"),
    DROP("OUT=5;0")
    ;

    private String command;

    Commands(String command) {
        this.command = command;
    }

    public String getCommand() {
        return "1;1;" + command;
    }
}
