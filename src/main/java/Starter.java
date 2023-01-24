import api.commands.MelfaBasic4CommandSet;
import api.control.RV3SB;
import api.control.RobotBuilder;
import api.nav.Position;
import yourprograms.RFSKlausur2Vereinfacht;

public class Starter {

    private static final String HOST = "192.168.1.223";
    private static final int PORT = 10001;

    private static final Position SAFE_POSITION = new Position(420.0, 0.0, 300.0, 180, 0, 180);

    public static void main(String[] args) {
        RV3SB robot = new RobotBuilder(HOST, PORT)
                .setSafePosition(SAFE_POSITION)
                .enableCommunication()
                .enableOperation()
                .enableServo()
                .exitOnError()
                .setSpeed(20)
                .setName("RV-3SB in F113")
                .setCommandSet(MelfaBasic4CommandSet.getCommandSet())
                .build();

        // Alternative way
        // robot = new RobotBuilder(HOST, PORT).buildPreConfig(SAFE_POSITION);

        robot.runProgram(new RFSKlausur2Vereinfacht());
    }

}
