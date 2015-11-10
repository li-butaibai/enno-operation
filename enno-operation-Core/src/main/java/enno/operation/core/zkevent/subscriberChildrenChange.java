package enno.operation.core.zkevent;

import enno.operation.zkl.ZKListener;

import java.util.Map;

/**
 * Created by v-zoli on 2015/11/10.
 */
public class subscriberChildrenChange implements ZKListener {
    public void process(Map<String, String> nodes) {
        if(!nodes.isEmpty())
        {}
    }
}
