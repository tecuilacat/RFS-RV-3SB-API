package com.github.tecuilacat.robotapi;

import com.github.tecuilacat.robotapi.api.commands.MelfaBasic4CommandSet;
import com.github.tecuilacat.robotapi.api.control.Robot;
import com.github.tecuilacat.robotapi.api.control.RobotBuilder;
import com.github.tecuilacat.robotapi.api.nav.Position;
import com.github.tecuilacat.robotapi.api.terminal.Terminal;

public class Starter {

    private static final String HOST = "192.168.1.223";
    private static final int PORT = 10001;

    private static final Position SAFE_POSITION = new Position(420.0, 0.0, 300.0, 180, 0, 180);

    public static void main(String[] args) {
        Robot robot = new RobotBuilder(HOST, PORT)
                .setSafePosition(SAFE_POSITION)
                .enableCommunication()
                .enableOperation()
                .enableServo()
                .setSpeed(10)
                .setName("RV-3SB")
                .setCommandSet(MelfaBasic4CommandSet.getCommandSet())
                .enableTerminalOperation()
                .build();

        Terminal terminal = new Terminal(robot);
        terminal.open();
    }

}