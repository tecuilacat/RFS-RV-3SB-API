package yourcode;

import api.control.RobotOperations;
import api.nav.Position;
import api.programs.RunnableProgram;

/**
 * Klausur Ende 2022 übersetzt in die eigene API
 */
public class RFSKlausur2Vereinfacht implements RunnableProgram {

    //Punkte
    private final Position PSTART = new Position(420.0, 0.0, 300.0, 180, 0, 180);
    private final Position P5 = new Position(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
    private final Position P11 = new Position(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
    private Position PAUF = new Position(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
    private Position PAB = new Position(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);

    //Unterprogramme
    private final RunnableProgram START = new SubSTART();
    private final RunnableProgram HOLEN = new SubHOLEN();
    private final RunnableProgram ABLAGE = new SubABLAGE();

    /**
     * Hauptprogramm
     */
    @Override
    public void runProgram(RobotOperations robot) {
        robot.setSafePosition(PSTART);

        robot.enableServo();
        PAUF = P11.copy();
        PAB = P5.copy();
        PAUF.setZ(PAUF.getZ() + 27.0); //9x3

        robot.runProgram(START);

        for (int i = 1; i <= 7; i++) {
            robot.runProgram(HOLEN);
            robot.runProgram(ABLAGE);
        }

        for (int i = 8; i <= 10; i++) {
            robot.runProgram(HOLEN);
            PAB.setC(PAB.getC() + 45.0);
            robot.runProgram(ABLAGE);
        }

        robot.disableServo();
    }

    /**
     * Unteraufgabe a)
     */
    private class SubSTART implements RunnableProgram {
        @Override
        public void runProgram(RobotOperations robot) {
            robot.movToSafePosition();
        }
    }

    /**
     * Unteraufgabe b)
     */
    private class SubHOLEN implements RunnableProgram {
        @Override
        public void runProgram(RobotOperations robot) {
            robot.movToPosition(PAUF, true);
            robot.grab();
            robot.mvsToPosition(PAUF.alterZ(-50));
            PAUF.setZ(PAUF.getZ() - 3.0);
        }
    }

    /**
     * Unteraufgabe c)
     */
    private class SubABLAGE implements RunnableProgram {
        @Override
        public void runProgram(RobotOperations robot) {
            robot.movToPosition(PAB, true);
            robot.drop();
            robot.mvsToPosition(PAB.alterZ(-50));
            PAB.setZ(PAB.getZ() + 3.0);
        }
    }
}
