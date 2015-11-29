package enno.operation.zkl;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * Created by EriclLee on 15/11/29.
 */
public class zkUtil {
    private static final ZKClient zkClient;
    static{
        try {
            ApplicationContext context=
                    new FileSystemXmlApplicationContext("/config/enno-operation/operation-server.xml");
            zkClient = (ZKClient) context.getBean("zkClient");
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }
    public static ZKClient getZkClient(){
        return zkClient;
    }
}
