package enno.operation.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by sabermai on 2015/11/19.
 */
public class testMain {
    private static Logger logger = LogManager.getLogger(testMain.class.getName());

    public static void main(String[] args) {
        logger.error("Test Log4j");
    }
}
