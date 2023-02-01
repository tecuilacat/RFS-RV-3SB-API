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
        return "1;1;SRVON";
    }

    @Override
    public String getServoOffCommand() {
        return "1;1;SRVOFF";
    }

    @Override
    public String getEnableCommunicationCommand() {
        return "1;1;OPEN=NARCUSR";
    }

    @Override
    public String getDisableCommunicationCommand() {
        return "1;1;CLOSE";
    }

    @Override
    public String getEnableOperationCommand() {
        return "1;1;CNTLON";
    }

    @Deprecated
    @Override
    public String getDisableOperationCommand() {
        return "1;1;CNTLOFF";
    }

    @Override
    public String getSetSpeedCommand() {
        return "1;1;OVRD=";
    }

    @Override
    public String getMOVCommand() {
        return "1;1;EXECMOV P_CURR+";
    }

    @Override
    public String getMVSCommand() {
        return "1;1;EXECMVS P_CURR+";
    }

    @Override
    public String getStateCommand() {
        return "1;1;STATE";
    }

    @Override
    public String getCurrentPositionCommand() {
        return "1;1;PPOSF";
    }

    @Override
    public String getGrabCommand() {
        return "1;1;OUT=5;1";
    }

    @Override
    public String getDropCommand() {
        return "1;1;OUT=5;0";
    }

}
