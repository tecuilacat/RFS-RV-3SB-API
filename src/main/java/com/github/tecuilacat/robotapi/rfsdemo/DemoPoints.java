package com.github.tecuilacat.robotapi.rfsdemo;

import com.github.tecuilacat.robotapi.api.nav.Position;

/**
 * Punkte für das Demo-Programm für den 16. März 2023
 */
public enum DemoPoints {

    PUFFER(new Position(300.0, 0.0, 300.0, 180.0, 0.0, 180.0)),
    FRAESE(new Position(300.0, 0.0, 300.0, 180.0, 0.0, 180.0)),
    PICKUP(new Position(392.0, 0.0, 150.0, 180.0, 0.0, 180.0)),
    POS1(new Position(220.0, -350.0, DemoConstants.Z_INDEX_ARBEITSPLATTE, 180.0, 0.0, 180.0)),
    POS2(new Position(-200.0, -340.0, DemoConstants.Z_INDEX_ARBEITSPLATTE, 180.0, 0.0, 180.0)),
    POS3(new Position(-150.0, -480.0, DemoConstants.Z_INDEX_ARBEITSPLATTE, 180.0, 0.0, 180.0)),
    POS4(new Position(200.0, -500.0, DemoConstants.Z_INDEX_ARBEITSPLATTE, 180.0, 0.0, 180.0)),
    POS5(new Position(0.0, -420.0, DemoConstants.Z_INDEX_ARBEITSPLATTE, 180.0, 0.0, 180.0))
    ;

    /**
     * Wird benötigt um Konstanten im Enum zu verwenden
     */
    private static class DemoConstants {
        private static final double Z_INDEX_ARBEITSPLATTE = 140.0;
    }

    private final Position position;

    DemoPoints(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }
}
