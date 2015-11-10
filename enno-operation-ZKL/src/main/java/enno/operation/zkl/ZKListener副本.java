package enno.operation.zkl;


import java.util.EventListener;
import java.util.Map;

/**
 * Created by v-zoli on 2015/10/22.
 */
public interface ZKListener extends EventListener {
    public void process(Map<String,String> nodes);
}
