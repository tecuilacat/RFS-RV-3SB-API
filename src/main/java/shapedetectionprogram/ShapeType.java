package shapedetectionprogram;

import api.nav.Position;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public enum ShapeType {

    CIRCLE("circle", 140.0, new Position(0,0,0,180.0, 0.0, 180.0)),
    SQUARE("square", 140.0, new Position(0,0,0,180.0, 0.0, 180.0)),
    OCTAGON("octagon", 140.0, new Position(0,0,0,180.0, 0.0, 180.0)),
    TRIANGLE("triangle", 140.0, new Position(0,0,0,180.0, 0.0, 180.0)),
    ;

    private String code;
    private double zIndexOnGrabPosition;
    private Position dropPosition;

    ShapeType(String code, double zIndexOnGrabPosition, Position dropPosition) {
        this.code = code;
        this.zIndexOnGrabPosition = zIndexOnGrabPosition;
        this.dropPosition = dropPosition;
    }

    public String getCode() {
        return code;
    }

    public double getzIndexOnGrabPosition() {
        return zIndexOnGrabPosition;
    }

    public Position getDropPosition() {
        return dropPosition;
    }

    public static ShapeType getByCode(String code) {
        return Arrays.stream(ShapeType.values())
                .filter(shapeType -> StringUtils.equalsIgnoreCase(code, shapeType.getCode()))
                .findFirst()
                .orElseThrow(() -> new EnumConstantNotPresentException(ShapeType.class, code));
    }
}
