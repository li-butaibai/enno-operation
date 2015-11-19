package enno.operation.core;

import enno.operation.core.common.LogUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by sabermai on 2015/11/19.
 */
public class testMain {
    private static Logger logger = LogManager.getLogger(testMain.class.getName());

    public static void main(String[] args) {
        try {
            Integer i = null;
            i.toString();
        } catch (Exception ex) {
            LogUtil.SaveLog(testMain.class.getName(), ex);
        }
    }
}
