package ZKAInterface;

import enno.operation.zkmodel.EventSourceConnectModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sabermai on 2015/11/30.
 */
public class ProcessSubscriberData implements IProcessSubscriberData{
    public Map<String, List<String>> processSubscriberData(String subscriberId, List<EventSourceConnectModel> connectModelList){
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        return map;
    }

    public List<EventSourceConnectModel> getOldSubscriberData(String subscriberId){
        List<EventSourceConnectModel> connectModelList = new ArrayList<EventSourceConnectModel>();
        return connectModelList;
    }
}
