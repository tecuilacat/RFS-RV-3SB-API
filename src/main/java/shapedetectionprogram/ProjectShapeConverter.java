package shapedetectionprogram;

import api.logger.Logger;
import api.nav.Position;
import api.parser.Parser;

import java.util.Objects;

public class ProjectShapeConverter implements Parser<String, Shape> {

    private static final Logger logger = new Logger(ProjectShapeConverter.class);

    @Override
    public Shape parse(String s) {
        String[] elements = s.split(";");
        if (elements.length == 3) {
            try {
                ShapeType shapeType = ShapeType.getByCode(elements[0]);
                if (Objects.nonNull(shapeType)) {
                    double x = Double.parseDouble(elements[1]);
                    double y = Double.parseDouble(elements[2]);
                    double z = shapeType.getzIndexOnGrabPosition();
                    Position pos = new Position(x, y, z, 180.0, 0.0, 180.0);
                    return new Shape(shapeType, pos);
                }
            } catch (Exception e) {
                logger.error(s + " kann nicht konvertiert werden");
            }
        } else {
            logger.error(s + " kann nicht konvertiert werden");
        }
        return null;
    }

}
