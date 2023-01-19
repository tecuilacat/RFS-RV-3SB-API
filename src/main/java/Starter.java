import api.control.RV3SB;
import api.control.RobotBuilder;
import api.logger.Logger;
import api.nav.Position;
import api.programs.RunnableProgram;
import yourcode.TestProgramForF113;

public class Starter {

    private static final Logger logger = new Logger(Starter.class);

    private static final String HOST = "192.168.1.223";
    private static final int PORT = 10001;

    private static final Position SAFE_POSITION = new Position(420.0, 0.0, 300.0, 180, 0, 180);

    public static void main(String[] args) {
        RV3SB robot = new RobotBuilder(HOST, PORT)
                .enableCommunication()
                .enableOperation()
                .enableServo()
                .setSpeed(10)
                .setSafePosition(SAFE_POSITION)
                .exitOnError()
                .build();

        RunnableProgram program = new TestProgramForF113();
        program.runProgram(robot);
    }

}
