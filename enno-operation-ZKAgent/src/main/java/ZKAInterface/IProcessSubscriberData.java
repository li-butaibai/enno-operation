package ZKAInterface;

import enno.operation.zkmodel.EventSourceConnectModel;

import java.util.List;
import java.util.Map;

/**
 * Created by sabermai on 2015/11/30.
 */
public interface IProcessSubscriberData {
    Map<String, List<EventSourceConnectModel>> processSubscriptions(String subscriberId, List<EventSourceConnectModel> addList, List<EventSourceConnectModel> removeList);

    List<EventSourceConnectModel> getCurrentSubscriberData(String subscriberId);
}
