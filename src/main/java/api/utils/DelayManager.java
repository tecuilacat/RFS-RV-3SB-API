package api.utils;

import api.logger.Logger;

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
     * @param timeout Milliseconds of timout in seconds
     */
    public static void defaultTimeout(double timeout) {
        try {
            Thread.sleep((long) timeout * 1000L);
        } catch (Exception e) {
            logger.error("Timeout konnte nicht ausgeführt werden", e);
        }
    }

}
