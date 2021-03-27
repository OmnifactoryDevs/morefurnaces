package dan.morefurnaces;

import org.apache.logging.log4j.Logger;

/**
 * Obligatory...
 *
 * GregTech logger
 * One edit to this class and you're not alive anymore
 */
public class MFLog {

    public static Logger logger;

    public static void init(Logger modLogger) {
        logger = modLogger;
    }
}
