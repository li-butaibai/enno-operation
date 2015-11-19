package enno.operation.core.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by sabermai on 2015/11/19.
 */
public class LogUtil {
    private String className;
    private static Logger logger;

    public static void SaveLog(String className, Exception ex) {
        logger = LogManager.getLogger(className);
        StackTraceElement[] elements = ex.getStackTrace();
        logger.error(ex);
        for (StackTraceElement element : elements) {
            logger.error(element.toString());
        }
    }
}
