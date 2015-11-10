import enno.operation.zkl.ZKListener;

import java.util.Map;

/**
 * Created by v-zoli on 2015/11/10.
 */
public class eventSourceDataChangeEvent implements ZKListener {
    public void process(Map<String, String> nodes) {
        for(Map.Entry<String,String> entry : nodes.entrySet())
        {
            System.out.println(String.format("EventSource data change: %s,%s",entry.getKey(), entry.getValue()));
        }
    }
}
