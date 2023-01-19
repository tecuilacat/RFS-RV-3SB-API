package utils;

import logger.Logger;

public class DelayManager {

    private static final Logger logger = new Logger(DelayManager.class);

    private static final int DEFAULT_TIMEOUT = 250;

    /**
     * @return Default timeout
     */
    public static int getDefaultTimeout() {
        return DEFAULT_TIMEOUT;
    }

    /**
     * Performes a timout on the timout set by default
     */
    public static void defaultTimeout() {
        try {
            Thread.sleep(DEFAULT_TIMEOUT);
        } catch (Exception e) {
            logger.error("Timeout konnte nicht ausgeführt werden", e);
        }
    }

    /**
     * Performs a timout for the time in milliseconds
     * @param timeout Milliseconds of timout
     */
    public static void defaultTimeout(int timeout) {
        try {
            Thread.sleep(timeout);
        } catch (Exception e) {
            logger.error("Timeout konnte nicht ausgeführt werden", e);
        }
    }

}
