package enno.operation.ZKListener;

import enno.operation.zkmodel.SubscriberData;

import java.util.EventListener;
import java.util.List;
import java.util.Map;

/**
 * Created by v-zoli on 2015/11/12.
 */
public interface SubscriberClusterListener extends EventListener  {
    public void process(List<SubscriberData> subscriberDataList);
}
