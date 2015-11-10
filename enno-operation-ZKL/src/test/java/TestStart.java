import enno.operation.zkl.ZKClient;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by EriclLee on 15/11/10.
 */
public class TestStart {
    private static ZKClient zkClient= new ZKClient();
    public static void main(String[] args )
    {
        try {
            Map<String, String> esList = new HashMap<String, String>();
            esList.put("amq01", "amq01data");
            esList.put("amq02", "amq02data");
            zkClient.setConnectString("127.0.0.1:2181");
            zkClient.setEventSourceChildNodeChangeEvent(new eventSourceChildChangeEvent());
            zkClient.setEventSourceDataChangeEvent(new eventSourceDataChangeEvent());
            zkClient.setSubscriberNodeChangeEvent(new subscriberNodeChangeEvent());
            zkClient.start(esList);
            System.in.read();
        }
        catch (Exception ex)
        {ex.printStackTrace();}
    }
}
