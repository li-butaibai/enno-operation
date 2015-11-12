import java.util.Map;

/**
 * Created by v-zoli on 2015/11/10.
 */
public class subscriberNodeChangeEvent implements ZKListener {
    public void process(Map<String,String> nodes)
    {
        if(nodes.isEmpty())
        {
            System.out.println("Clear!");
        }
        for(Map.Entry<String,String> entry : nodes.entrySet())
        {
            System.out.println(String.format("Subscriber node change: %s,%s",entry.getKey(), entry.getValue()));
        }
    }
}
