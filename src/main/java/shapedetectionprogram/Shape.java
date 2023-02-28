package shapedetectionprogram;

import api.nav.Position;

public class Shape {

    private ShapeType shapeType;

    private Position position;

    public Shape(ShapeType shapeType, Position position) {
        this.shapeType = shapeType;
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public ShapeType getShapeType() {
        return shapeType;
    }

    public void setShapeType(ShapeType shapeType) {
        this.shapeType = shapeType;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
