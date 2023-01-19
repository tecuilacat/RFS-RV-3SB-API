package api.parser;

import api.logger.Logger;
import api.state.RobotState;

public class RobotStateParser implements Parser<String, RobotState> {

    private static final Logger logger = new Logger(RobotStateParser.class);

    @Override
    public RobotState parse(String robotAnswer) {
        try {
            String[] elements = robotAnswer.split(";");
            int speed = Integer.parseInt(elements[2]);
            boolean isMoving = elements[18].charAt(0) != '0'; //After '0' there are several null Objects which is why only the first char is needed
            return new RobotState(
                    speed,
                    isMoving
            );
        } catch (Exception e) {
            logger.debug("Error occurred parsing response - previous action might not have been completed yet");
        }
        return new RobotState(
                0,
                true //ensures, that in case of an error, no new command is sent to the robot
        );
    }

}
