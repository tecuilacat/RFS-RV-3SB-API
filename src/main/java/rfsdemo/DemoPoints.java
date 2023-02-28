package rfsdemo;

import api.nav.Position;

/**
 * Punkte für das Demo-Programm für den 16. März 2023
 */
public enum DemoPoints {

    PUFFER(new Position(0.0, 0.0, 300.0, 180.0, 0.0, 180.0)),
    FRAESE(new Position(0.0, 0.0, 300.0, 180.0, 0.0, 180.0)),
    PICKUP(new Position(0.0, 0.0, 300.0, 180.0, 0.0, 180.0)),
    POS1(new Position(0.0, 0.0, 300.0, 180.0, 0.0, 180.0)),
    POS2(new Position(0.0, 0.0, 300.0, 180.0, 0.0, 180.0)),
    POS3(new Position(0.0, 0.0, 300.0, 180.0, 0.0, 180.0)),
    POS4(new Position(0.0, 0.0, 300.0, 180.0, 0.0, 180.0)),
    POS5(new Position(0.0, 0.0, 300.0, 180.0, 0.0, 180.0)),
    ;

    private Position position;

    DemoPoints(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }
}
