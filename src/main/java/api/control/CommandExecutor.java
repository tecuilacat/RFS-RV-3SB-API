package api.control;

import api.logger.Logger;
import api.utils.DelayManager;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Sends and executes commands
 */
public class CommandExecutor {

    private static final Logger logger = new Logger(CommandExecutor.class);

    private OutputStream writer;
    private InputStream reader;

    public CommandExecutor(InputStream reader, OutputStream writer) {
        this.reader = reader;
        this.writer = writer;
    }

    /**
     * Used for all commands except for those that move the robot
     *
     * @author Former RFS-group from a couple of years | NOT me
     * @param command Command the robot has to execute
     * @return Answer of the robot
     */
    public String execute(String command) {
        logger.command("Executing command: " + command);
        String res = "";
        try {
            writer.write(command.getBytes(StandardCharsets.US_ASCII));
            DelayManager.defaultTimeout();
            byte[] answer = new byte[1024];
            reader.read(answer);
            res = new String(answer);
        } catch (Exception e) {
            logger.error("Command " + command + " could not be executed", e);
        }
        return res;
    }

}
