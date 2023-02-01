package yourprograms;

import api.control.RobotOperations;
import api.nav.Position;
import api.programs.RunnableProgram;

public class FS465_A_Palettierung_Var implements RunnableProgram {

    private static final Position P11 = new Position(+34.00,-496.00,+128.00,+180.00,+0.00,-90.00);
    private static final Position PPASTA = new Position(+370.00,-80.00,+136.00,+180.00,+0.00,+0.00);
    private static final Position PGRUND = new Position(+250.00,-250.00,+320.00,-180.00,+0.00,-180.00);

    private static final int rVeZeil = 90;
    private static final int rVeSpal = 130;

    private static final int BLECH_DICKE = 3;

    private Position PGRAB;
    private Position PDROP;

    @Override
    public void runProgram(RobotOperations robot) {
        robot.runProgram(MAIN);
    }

    private final RunnableProgram INIT = new RunnableProgram() {
        @Override
        public void runProgram(RobotOperations robot) {

        }
    };

    private final RunnableProgram MAIN = new RunnableProgram() {
        @Override
        public void runProgram(RobotOperations robot) {
            robot.runProgram(START);
            PGRAB = P11.copy();
            PGRAB.setZ(PGRAB.getZ() + (11 * BLECH_DICKE));

            PDROP = PPASTA.copy();

            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    PDROP.setY(PPASTA.getY());
                    for (int k = 0; k < 3; k++) {
                        robot.runProgram(PICK);
                        robot.runProgram(PLACET);
                        PDROP.setY(PDROP.getY() + rVeZeil);
                    }
                    PDROP.setX(PPASTA.getX() + rVeSpal);
                }
                PDROP.setZ(PDROP.getZ() + BLECH_DICKE);
            }
        }
    };

    private final RunnableProgram START = new RunnableProgram() {
        @Override
        public void runProgram(RobotOperations robot) {
            robot.setSafePosition(PGRUND);
            if (!robot.getCurrentPosition().equals(PGRUND)) {
                robot.mvsToPosition(robot.getCurrentPosition().alterAbsoluteZ(300));
            }
            robot.movToSafePosition();
        }
    };

    private final RunnableProgram PICK = new RunnableProgram() {
        @Override
        public void runProgram(RobotOperations robot) {
            robot.movToPositionWithSafeTravel(PGRAB);
            robot.grab();
            robot.mvsToPosition(PGRAB.alterZ(-50));
            PGRAB.setZ(PGRAB.getZ() - BLECH_DICKE);
        }
    };

    private final RunnableProgram PLACET = new RunnableProgram() {
        @Override
        public void runProgram(RobotOperations robot) {
            robot.movToPositionWithSafeTravel(PDROP);
            robot.grab();
            robot.mvsToPosition(PDROP.alterZ(-50));
        }
    };
}
