package enno.operation.core.zkevent;

import enno.operation.zkl.ZKListener;

import java.util.Map;

/**
 * Created by v-zoli on 2015/11/10.
 */
public class subscriberChildrenChange implements ZKListener {
    public void process(Map<String, String> nodes) {
        if(nodes.isEmpty())
        {
            //TODO: 将所有订阅者状态置为离线，DB
            //TODO：更新所有EventSource状态置为空闲（DB，ZK）
        }
        else
        {
            //TODO：与数据库数据比较找到所有刚离线的订阅者
            //TODO: 将这些订阅者状态置为离线，DB
            //TODO：更新这些订阅者订阅的所有EventSource状态置为空闲（DB，ZK）

            //TODO：更新nodes对应订阅者的状态为上线，如果是新上线的则注册到数据库
        }
    }
}
