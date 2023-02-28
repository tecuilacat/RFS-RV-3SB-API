package rfsdemo;

import api.control.RobotOperations;
import api.programs.RunnableProgram;

/**
 * - Es befinden sich 5 Werkstücke auf der Arbeitsfläche
 * - Das im Kreis wird auf die Fräse gelegt
 * - Die anderen Werkstücke rücken nach
 * - Das Werkstück wird von der Fräse auf das Ende der Schlange gestellt
 * - Durch die Endlosschleife sieht es aus wie ein Kreislauf
 *
 */
public class DemoProgram implements RunnableProgram {

    private static boolean keepRunning = true;

    @Override
    public void runProgram(RobotOperations robot) {
        robot.runProgram(new PickupHelper()); //NUR ZUM INITIALISIEREN!!

        MainProgram demo = new MainProgram();

        while (keepRunning) {
            robot.runProgram(demo);
        }

        robot.delay(1);
        robot.drop();
        robot.disableServo();
    }

    private void moveObjectFromTo(RobotOperations ops, DemoPoints from, DemoPoints to) {
        ops.movToPositionWithSafeTravel(from.getPosition());
        ops.grab();
        ops.mvsToPosition(from.getPosition().alterZ(-50));
        ops.movToPositionWithSafeTravel(to.getPosition());
        ops.drop();
        ops.mvsToPosition(to.getPosition().alterZ(-50));
    }

    private class MainProgram implements RunnableProgram {
        @Override
        public void runProgram(RobotOperations robot) {
            robot.runProgram(new ToFraese());
            robot.runProgram(new Rearrange());
            robot.runProgram(new FromFraese());
        }
    }

    /**
     * Sortiert die Wekstücke um
     * Pos1 -> Pos2
     * Pos2 -> Pos3
     * Pos3 -> Pos4
     * Pos4 -> Pos5
     */
    private class Rearrange implements RunnableProgram {
        @Override
        public void runProgram(RobotOperations robot) {
            moveObjectFromTo(robot, DemoPoints.POS4, DemoPoints.POS5);
            moveObjectFromTo(robot, DemoPoints.POS3, DemoPoints.POS4);
            moveObjectFromTo(robot, DemoPoints.POS2, DemoPoints.POS3);
            moveObjectFromTo(robot, DemoPoints.POS1, DemoPoints.POS2);
        }
    }

    /**
     * Geht über einen Pufferpunkt von Position 5 zur Fräse
     * Puffer -> Pos5 -> grab -> P_Fräse -> drop -> Puffer
     */
    private class ToFraese implements RunnableProgram {
        @Override
        public void runProgram(RobotOperations robot) {
            robot.movToPosition(DemoPoints.PUFFER.getPosition());
            moveObjectFromTo(robot, DemoPoints.POS5, DemoPoints.FRAESE);
            robot.movToPosition(DemoPoints.PUFFER.getPosition());
        }
    }

    /**
     * Geht über einen sicheren Pufferpunkt von der Fräse zu Position 1
     * Puffer -> P_Fräse -> grab -> Pos1 -> drop -> Puffer
     */
    private class FromFraese implements RunnableProgram {
        @Override
        public void runProgram(RobotOperations robot) {
            robot.movToPosition(DemoPoints.PUFFER.getPosition());
            moveObjectFromTo(robot, DemoPoints.FRAESE, DemoPoints.POS1);
            robot.movToPosition(DemoPoints.PUFFER.getPosition());
        }
    }

    /**
     * Wird nur ganz am Anfang benötigt um die Werkstücke zu platzieren
     */
    private class PickupHelper implements RunnableProgram {
        @Override
        public void runProgram(RobotOperations robot) {
            moveObjectFromTo(robot, DemoPoints.PICKUP, DemoPoints.POS1);
            robot.movToPosition(DemoPoints.PUFFER.getPosition());
            moveObjectFromTo(robot, DemoPoints.PICKUP, DemoPoints.POS2);
            robot.movToPosition(DemoPoints.PUFFER.getPosition());
            moveObjectFromTo(robot, DemoPoints.PICKUP, DemoPoints.POS3);
            robot.movToPosition(DemoPoints.PUFFER.getPosition());
            moveObjectFromTo(robot, DemoPoints.PICKUP, DemoPoints.POS4);
            robot.movToPosition(DemoPoints.PUFFER.getPosition());
            moveObjectFromTo(robot, DemoPoints.PICKUP, DemoPoints.POS5);
            robot.movToPosition(DemoPoints.PUFFER.getPosition());
        }
    }
}
