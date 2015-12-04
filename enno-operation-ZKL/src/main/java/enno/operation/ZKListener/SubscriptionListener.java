package enno.operation.ZKListener;

import enno.operation.zkl.ZKAgent;
import enno.operation.zkmodel.EventSourceConnectModel;
import org.apache.zookeeper.KeeperException;

import java.util.EventListener;
import java.util.List;
import java.util.Map;

/**
 * Created by sabermai on 2015/12/1.
 */
public interface SubscriptionListener extends EventListener {
    List<EventSourceConnectModel> process( String subscriberId, List<EventSourceConnectModel> connectModelList, List<EventSourceConnectModel> subscriberData) throws KeeperException, InterruptedException;
}
