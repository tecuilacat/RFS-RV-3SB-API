package yourprograms;

import api.control.RobotOperations;
import api.nav.Position;
import api.programs.RunnableProgram;

import java.util.ArrayList;
import java.util.List;

public class DetectShapesAndSort implements RunnableProgram {

    /**
     * Wird in der Form vom python script mit Bilderkennung generiert
     */
    private static class ShapeToSort {
        private Position from;
        private Position to;
        public Position getFrom() {
            return from;
        }
        public Position getTo() {
            return to;
        }
    }

    private List<ShapeToSort> shapes;

    @Override
    public void runProgram(RobotOperations robot) {
        fillShapesToSort();

        robot.movToSafePosition();

        for (ShapeToSort shape: shapes) {
            robot.movToPositionWithSafeTravel(shape.getFrom());
            robot.grab();
            robot.mvsToPosition(shape.getFrom().alterZ(-50));

            robot.delay(1.5);

            robot.movToPositionWithSafeTravel(shape.getTo());
            robot.drop();
            robot.mvsToPosition(shape.getTo().alterZ(-50));
        }

        robot.movToSafePosition();
    }

    private void fillShapesToSort() {
        //run python script to fill list
        shapes = new ArrayList<>();
    }

}
