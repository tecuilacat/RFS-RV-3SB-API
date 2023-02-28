package shapedetectionprogram;

import api.control.RobotOperations;
import api.logger.Logger;
import api.nav.Position;
import api.programs.RunnableProgram;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ProjectProgram implements RunnableProgram {

    private static final Logger logger = new Logger(ProjectProgram.class);

    private static final Position MIN_POSITION_GRAB_ZONE = new Position(0.0, 0.0, 0.0, 180.0, 0.0, 180.0); //TODO herausfinden
    private static final Position MAX_POSITION_GRAB_ZONE = new Position(100.0, 100.0, 0.0, 180.0, 0.0, 180.0); //TODO herausfinden

    private static final double CAMERA_RESOLUTION_WIDTH = 1880; //TODO herausfinden
    private static final double CAMERA_RESOLUTION_HEIGHT = 935; //TODO herausfinden

    @Override
    public void runProgram(RobotOperations robot) {
        logger.info("Platziere die Schüssel für Quadrate an folgende Stelle");
        robot.movToPositionWithSafeTravel(ShapeType.SQUARE.getDropPosition());
        robot.delay(2);

        logger.info("Platziere die Schüssel für Kreise an folgende Stelle");
        robot.movToPositionWithSafeTravel(ShapeType.CIRCLE.getDropPosition());
        robot.delay(2);

        logger.info("Platziere die Schüssel für Oktagone an folgende Stelle");
        robot.movToPositionWithSafeTravel(ShapeType.OCTAGON.getDropPosition());
        robot.delay(2);

        logger.info("Platziere die Schüssel für Dreiecke an folgende Stelle");
        robot.movToPositionWithSafeTravel(ShapeType.TRIANGLE.getDropPosition());
        robot.delay(2);

        List<Shape> foundShapes = parsePositions(Pythonrunner.runPythonFile("src\\main\\java\\shapedetectionprogram\\detect.py"));
        foundShapes = translateToRobotCoordinateSystem(foundShapes);
        for (Shape shape: foundShapes) {
            robot.movToPositionWithSafeTravel(shape.getPosition());
            robot.grab();
            robot.mvsToPosition(shape.getPosition().alterZ(-50));
            robot.movToPositionWithSafeTravel(shape.getShapeType().getDropPosition());
            robot.drop();
            robot.mvsToPosition(robot.getCurrentPosition().alterZ(-100));
        }
        robot.movToSafePosition();
    }

    public static void main(String[] args) {
        List<String> result = Pythonrunner.runPythonFile("src\\main\\java\\shapedetectionprogram\\detect.py");
        List<Shape> foundShapes = parsePositions(result);

        foundShapes = translateToRobotCoordinateSystem(foundShapes);
        foundShapes.forEach(s -> logger.info(s.getShapeType().getCode() + s.getPosition()));
    }

    private static List<Shape> translateToRobotCoordinateSystem(List<Shape> shapes) {
        List<Shape> result = new ArrayList<>();

        double widthGrabZone = MAX_POSITION_GRAB_ZONE.getX() - MIN_POSITION_GRAB_ZONE.getX();
        double heightGrabZone = MAX_POSITION_GRAB_ZONE.getY() - MIN_POSITION_GRAB_ZONE.getY();

        double factorX = widthGrabZone / CAMERA_RESOLUTION_WIDTH;
        double factorY = heightGrabZone / CAMERA_RESOLUTION_HEIGHT;

        for (Shape shape: shapes) {
            Position position = shape.getPosition();
            position.setX(factorX * position.getX());
            position.setY(factorY * position.getY());
            shape.setPosition(position);
            result.add(shape);
        }
        return result;
    }

    private static List<Shape> parsePositions(List<String> lines) {
        ProjectShapeConverter parser = new ProjectShapeConverter();
        return lines.stream()
                .map(parser::parse)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

}
