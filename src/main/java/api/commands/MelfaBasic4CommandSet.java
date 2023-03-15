package api.commands;

import java.util.Objects;

public class MelfaBasic4CommandSet implements CommandSet {

    private static MelfaBasic4CommandSet instance;

    /**
     * @return Set of commands for the robot in the language Melfa Basic 4
     */
    public static MelfaBasic4CommandSet getCommandSet() {
        if (Objects.isNull(instance)) {
            instance = new MelfaBasic4CommandSet();
        }
        return instance;
    }

    private MelfaBasic4CommandSet() {}

    @Override
    public String getServoOnCommand() {
        return "SRVON";
    }

    @Override
    public String getServoOffCommand() {
        return "SRVOFF";
    }

    @Override
    public String getEnableCommunicationCommand() {
        return "OPEN=NARCUSR";
    }

    @Override
    public String getDisableCommunicationCommand() {
        return "CLOSE";
    }

    @Override
    public String getEnableOperationCommand() {
        return "CNTLON";
    }

    @Deprecated
    @Override
    public String getDisableOperationCommand() {
        return "CNTLOFF";
    }

    @Override
    public String getSetSpeedCommand() {
        return "OVRD=";
    }

    @Override
    public String getMOVCommand() {
        return "EXECMOV P_CURR+";
    }

    @Override
    public String getMVSCommand() {
        return "EXECMVS P_CURR+";
    }

    @Override
    public String getStateCommand() {
        return "STATE";
    }

    @Override
    public String getCurrentPositionCommand() {
        return "PPOSF";
    }

    @Override
    public String getGrabCommand() {
        return "OUT=5;1";
    }

    @Override
    public String getDropCommand() {
        return "OUT=5;0";
    }

    @Override
    public String getNotifyNcMachine() {
        return "OUT=6;1";
    }

    @Override
    public String getResetNcMachine() {
        return "OUT=6;0";
    }
}
