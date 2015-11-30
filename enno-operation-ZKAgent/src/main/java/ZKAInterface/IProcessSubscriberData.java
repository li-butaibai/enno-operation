package ZKAInterface;

import enno.operation.zkmodel.EventSourceConnectModel;

import java.util.List;
import java.util.Map;

/**
 * Created by sabermai on 2015/11/30.
 */
public interface IProcessSubscriberData {
    Map<String, List<String>> processSubscriberData(String subscriberId, List<EventSourceConnectModel> connectModelList);

    List<EventSourceConnectModel> getOldSubscriberData(String subscriberId);
}
