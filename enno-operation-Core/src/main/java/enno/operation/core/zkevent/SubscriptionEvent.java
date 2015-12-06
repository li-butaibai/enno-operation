package enno.operation.core.zkevent;

import enno.operation.ZKListener.SubscriptionListener;
import enno.operation.zkl.ZKAgent;
import enno.operation.zkmodel.EventSourceConnectModel;
import org.apache.zookeeper.KeeperException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.util.*;

/**
 * Created by sabermai on 2015/12/1.
 */
public class SubscriptionEvent implements SubscriptionListener {

    public List<EventSourceConnectModel> process(String subscriberId, List<EventSourceConnectModel> connectModelList, List<EventSourceConnectModel> subscriberData) throws KeeperException, InterruptedException {
        ZKAgent zkAgent = null;
        ApplicationContext context=
                new FileSystemXmlApplicationContext("/config/enno-operation/operation-server.xml");
        zkAgent = (ZKAgent) context.getBean("zkAgent");
        zkAgent.connect();
        List<EventSourceConnectModel> addData = new ArrayList<EventSourceConnectModel>(connectModelList);
        List<EventSourceConnectModel> removeData = new ArrayList<EventSourceConnectModel>(subscriberData);
        addData.removeAll(subscriberData);
        removeData.removeAll(connectModelList);

        for (EventSourceConnectModel esc : addData) {
            boolean result = processSubscription();
            if (result) {
                zkAgent.CreateSubscriberNodeUnderEventSource(subscriberId, esc.getSourceId());
                subscriberData.add(esc);
            }
            zkAgent.CreateSubscribeConnectLogNode(subscriberId, esc.getSourceId(), result);
        }

        for (EventSourceConnectModel esc : removeData) {
            boolean result = processSubscription();
            if (result) {
                zkAgent.DeleteSubscriberNodeUnderEventSource(subscriberId, esc.getSourceId());
                subscriberData.remove(esc);
            }
            zkAgent.CreateUnsubscribeConnectLogNode(subscriberId, esc.getSourceId(), result);
        }

        return subscriberData;
    }

    private boolean processSubscription() {
        Random random = new Random();
        if (Math.abs(random.nextInt()) % 10 > 1)
            return true;
        else
            return false;
    }
}
