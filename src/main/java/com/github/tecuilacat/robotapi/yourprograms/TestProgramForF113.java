package com.github.tecuilacat.robotapi.yourprograms;

import com.github.tecuilacat.robotapi.api.control.RobotOperations;
import com.github.tecuilacat.robotapi.api.logger.Logger;
import com.github.tecuilacat.robotapi.api.nav.Position;
import com.github.tecuilacat.robotapi.api.programs.RunnableProgram;
import com.github.tecuilacat.robotapi.api.utils.DelayManager;

public final class TestProgramForF113 implements RunnableProgram {

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

            DelayManager.sleep(3);

            logger.info("Testing MOV commands WITH safe travel");

            robot.movToPositionWithSafeTravel(P1);
            robot.movToPositionWithSafeTravel(P2);
            robot.movToPositionWithSafeTravel(P3);
            robot.movToPositionWithSafeTravel(P4);
            robot.movToPositionWithSafeTravel(P1);
            robot.movToSafePosition();

            DelayManager.sleep(3);

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

            robot.grab();
            DelayManager.sleep(3);
            robot.drop();

            logger.info("Testing done");

            DelayManager.sleep(3);

            robot.movToSafePosition();

        } catch (Exception e) {
            logger.error("Executing the program failed", e);
        } finally {
            logger.info("Done testing. Shutting down robot");
            robot.disableServo();
        }
    }
}
