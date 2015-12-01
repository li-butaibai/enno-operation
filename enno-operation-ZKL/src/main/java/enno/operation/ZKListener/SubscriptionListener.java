package enno.operation.ZKListener;

import enno.operation.zkmodel.EventSourceConnectModel;

import java.util.EventListener;
import java.util.List;
import java.util.Map;

/**
 * Created by sabermai on 2015/12/1.
 */
public interface SubscriptionListener extends EventListener {
    Map<String, List<EventSourceConnectModel>> process(String subscriberId, List<EventSourceConnectModel> addList, List<EventSourceConnectModel> removeList);
}
