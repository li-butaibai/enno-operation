package enno.operation.zkl;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * Created by sabermai on 2015/12/1.
 */
public class ZKAgentUtil {
    private ZKAgent zkAgent;
    public ZKAgent newZKAgentInstance(String subscriberId, String comments){
        try {
            ApplicationContext context=
                    new FileSystemXmlApplicationContext("/config/enno-operation/operation-server.xml");
            zkAgent = (ZKAgent) context.getBean("zkAgent");
            zkAgent.setSubscriberId(subscriberId);
            zkAgent.setComments(comments);
            zkAgent.connect();
            zkAgent.initializeZKAgent();
            return zkAgent;
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }
}
