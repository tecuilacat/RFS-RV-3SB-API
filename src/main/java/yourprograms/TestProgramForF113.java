package yourprograms;

import api.control.RobotOperations;
import api.logger.Logger;
import api.nav.Position;
import api.programs.RunnableProgram;

public class TestProgramForF113 implements RunnableProgram {

    private static final Logger logger = new Logger(TestProgramForF113.class);

    private final Position P1 = new Position(300.0, -400, 300, 180.0, 0.0, 180.0);
    private final Position P2 = new Position(-200.0, -400, 300, 180.0, 0.0, 180.0);
    private final Position P3 = new Position(-200.0, -500, 300, 180.0, 0.0, 180.0);
    private final Position P4 = new Position(300.0, -500, 300, 180.0, 0.0, 180.0);

    @Override
    public void runProgram(RobotOperations robot) {
        try {
            logger.info("Starting program");
            logger.info("Testing MOV commands WITHOUT safe travel");

            robot.movToPositionWithSafeTravel(P1);
            robot.movToPositionWithSafeTravel(P2);
            robot.movToPositionWithSafeTravel(P3);
            robot.movToPositionWithSafeTravel(P4);
            robot.movToPositionWithSafeTravel(P1);
            robot.movToSafePosition();

            logger.info("Testing MOV commands WITH safe travel");

            robot.movToPositionWithSafeTravel(P1);
            robot.movToPositionWithSafeTravel(P2);
            robot.movToPositionWithSafeTravel(P3);
            robot.movToPositionWithSafeTravel(P4);
            robot.movToPositionWithSafeTravel(P1);
            robot.movToSafePosition();

            logger.info("Now testing MVS Commands");

            robot.mvsToPosition(P1.alterZ(-50.0));
            robot.mvsToPosition(P1);
            robot.mvsToPosition(P2.alterZ(-50.0));
            robot.mvsToPosition(P2);
            robot.mvsToPosition(P3.alterZ(-50.0));
            robot.mvsToPosition(P3);
            robot.mvsToPosition(P4.alterZ(-50.0));
            robot.mvsToPosition(P4);
            robot.mvsToPosition(P1.alterZ(-50.0));
            robot.mvsToPosition(P1);
            robot.movToSafePosition();

        } catch (Exception e) {
            logger.error("Executing the program failed", e);
        } finally {
            logger.info("Done testing. Shutting down robot");
            robot.disableServo();
        }
    }
}
