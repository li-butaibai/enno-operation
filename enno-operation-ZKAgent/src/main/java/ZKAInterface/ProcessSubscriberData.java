package ZKAInterface;

import enno.operation.zkmodel.EventSourceConnectModel;

import java.util.*;

/**
 * Created by sabermai on 2015/11/30.
 */
public class ProcessSubscriberData implements IProcessSubscriberData{
    public Map<String, List<EventSourceConnectModel>> processSubscriptions(String subscriberId, List<EventSourceConnectModel> addList, List<EventSourceConnectModel> removeList){
        Map<String, List<EventSourceConnectModel>> map = new HashMap<String, List<EventSourceConnectModel>>();
        List<EventSourceConnectModel> addResult = new ArrayList<EventSourceConnectModel>();
        List<EventSourceConnectModel> removeResult = new ArrayList<EventSourceConnectModel>();

        for(EventSourceConnectModel esc:addList){
            Random random = new Random();
            if(Math.abs(random.nextInt()) % 10 > 1){
                addResult.add(esc);
            }
        }

        for(EventSourceConnectModel esc:removeList){
            Random random = new Random();
            if(Math.abs(random.nextInt()) % 10 > 1){
                removeResult.add(esc);
            }
        }
        map.put("add",addResult);
        map.put("remove",removeResult);

        return map;
    }

    public List<EventSourceConnectModel> getCurrentSubscriberData(String subscriberId) {
        return null;
    }
}
