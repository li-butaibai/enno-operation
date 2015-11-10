package enno.operation.zkl;

import org.apache.zookeeper.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by v-zoli on 2015/10/22.
 */
public class ZKClient {
    private ZooKeeper zooKeeper = null;
    private ZKListener subscriberNodeChangeEvent = null;
    private ZKListener eventSourceDataChangeEvent = null;
    private ZKListener eventSourceChildNodeChangeEvent = null;
    private String connectString = "";
    private int sessionTimeout = 1000;
    //    private long sessionId = 0;
    //private String sessionPasswd = "";
    //private Map<String, String> eventSourceList = new HashMap<String, String>();
    private String subscriberRootName = "/EnnoClusterRoot";
    private String eventSourceRootName = "/EventSourceRoot";

    public ZKClient() {

    }

    public void start(Map<String, String> eventSourceList) {
        try {
            zooKeeper = new ZooKeeper(connectString, sessionTimeout, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        initializeSchema(eventSourceList);
    }

    private  void  processChildNodeAndData(List<String> nodes,ZKListener zkListener)
    {
        Map<String, String> nodeDatas = null;
        try {
            nodeDatas = new HashMap<String, String>();
            for (String node : nodes) {
                String nodeData = new String(zooKeeper.getData(node, false, null));
                nodeDatas.put(node, nodeData);
            }
            if (zkListener != null) {
                zkListener.process(nodeDatas);
            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    private void processNodeAndData(String node,String data, ZKListener zkListener)
    {
        Map<String, String> nodeDatas = null;
        try {
            nodeDatas = new HashMap<String, String>();
            nodeDatas.put(node, data);
            if (zkListener != null) {
                zkListener.process(nodeDatas);
            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    //initialize the zookeeper schema, and add the watch to the event
    private boolean initializeSchema(Map<String, String> eventSourceList) {
        try {
            //initialize the enno cluster root node
            boolean newSubscriberRoot = false;
            if (zooKeeper.exists(subscriberRootName, false) == null) {
                zooKeeper.create(subscriberRootName, subscriberRootName.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                newSubscriberRoot = true;
            }
            // watch the enno cluster root child node
            List<String> ennoClusterChildrenNodes = zooKeeper.getChildren(subscriberRootName, new Watcher() {
                //@Override
                public void process(WatchedEvent event) {
                    try {
                        List<String> ennoClusterChildrenNodes = zooKeeper.getChildren(event.getPath(), this, null);
                        processChildNodeAndData(ennoClusterChildrenNodes, subscriberNodeChangeEvent);
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            }, null);
            if (newSubscriberRoot == false) {
                processChildNodeAndData(ennoClusterChildrenNodes, subscriberNodeChangeEvent);
            }

            //initialize the event source root node
            if (zooKeeper.exists(eventSourceRootName, false) == null) {
                zooKeeper.create(eventSourceRootName, eventSourceRootName.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            //initialize the sub event source node
            for (Map.Entry<String, String> eventSource : eventSourceList.entrySet()) {
                String path = eventSourceRootName + "/" + eventSource.getKey();
                //create the sub event source node
                boolean newEventSource = false;
                if (zooKeeper.exists(path, false) == null) {
                    zooKeeper.create(path, eventSource.getValue().getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                    newEventSource = true;
                }
                //watch the event source node data change event
                byte[] nodeData = zooKeeper.getData(path, new Watcher() {
                    //@Override
                    public void process(WatchedEvent event) {
                        try {
                            String nodeData = new String(zooKeeper.getData(event.getPath(), this, null));
                            processNodeAndData(event.getPath(),nodeData, eventSourceDataChangeEvent);
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                }, null);
                if (newEventSource) {
                    processNodeAndData(path, new String(nodeData), eventSourceDataChangeEvent);
                }
                //watch the event source children node change event
                List<String> eventSourceChildrenNodes = zooKeeper.getChildren(path, new Watcher() {
                    //@Override
                    public void process(WatchedEvent event) {
                        try {
                            List<String> eventSourceChildrenNodes = zooKeeper.getChildren(event.getPath(), this, null);
                            processChildNodeAndData(eventSourceChildrenNodes,eventSourceChildNodeChangeEvent);
                            for(String child : eventSourceChildrenNodes)
                            {
                                zooKeeper.delete(child,-1);
                            }
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                }, null);
                processChildNodeAndData(eventSourceChildrenNodes, eventSourceChildNodeChangeEvent);
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    //add new event source to zookeeper
    public void addEventSource(String eventSourceName, String eventSourceData)
    {
        try {
            String path = eventSourceRootName + "/" + eventSourceName;
            if (zooKeeper.exists(path, false) == null) {
                zooKeeper.create(path, eventSourceData.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                //watch the event source node data change event
                zooKeeper.getData(path, new Watcher() {
                    //@Override
                    public void process(WatchedEvent event) {
                        try {
                            String nodeData = new String(zooKeeper.getData(event.getPath(), this, null));
                            processNodeAndData(event.getPath(),nodeData, eventSourceDataChangeEvent);
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                }, null);
                //watch the event source children node change event
                zooKeeper.getChildren(path, new Watcher() {
                    //@Override
                    public void process(WatchedEvent event) {
                        try {
                            List<String> eventSourceChildrenNodes = zooKeeper.getChildren(event.getPath(), this, null);
                            processChildNodeAndData(eventSourceChildrenNodes, eventSourceChildNodeChangeEvent);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }, null);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    //remove event source from zookeeper
    public void removeEventSource(String eventSourceName)
    {
        try {
            String path = eventSourceRootName + "/" + eventSourceName;
            if (zooKeeper.exists(path, false) != null) {
                zooKeeper.delete(path, -1);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void setSubscriberData(String subscriberId, String data)
    {
        try{
            String path = String.format("%s/%s", subscriberRootName, subscriberId);
            if(zooKeeper.exists(path, false) != null) {
                zooKeeper.setData(path, data.getBytes(), -1);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void setEventSourceData(String eventSourceId, String data)
    {
        try{
            String path = String.format("%s/%s", eventSourceRootName, eventSourceId);
            if(zooKeeper.exists(path, false) != null) {
                zooKeeper.setData(path, data.getBytes(), -1);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public Map<String,String> getEventSourceList() {
        Map<String,String> eventSourceList = new HashMap<String, String>();
        try {
            List<String> children = zooKeeper.getChildren(eventSourceRootName, false);
            for(String child : children)
            {
                String childData = new String(zooKeeper.getData(child,false,null));
                eventSourceList.put(child,childData);
            }
        }
        catch (Exception ex)
        {
            eventSourceList.clear();
            ex.printStackTrace();
        }
        return eventSourceList;
    }

    public Map<String, String> getSubscriberList() {
        Map<String,String> subscriberList = new HashMap<String, String>();
        try {
            List<String> children = zooKeeper.getChildren(subscriberRootName, false);
            for(String child : children)
            {
                String childData = new String(zooKeeper.getData(child,false,null));
                subscriberList.put(child, childData);
            }
        }
        catch (Exception ex)
        {
            subscriberList.clear();
            ex.printStackTrace();
        }
        return subscriberList;
    }

    public ZooKeeper getZooKeeper() {
        return zooKeeper;
    }

    public void setSubscriberNodeChangeEvent(ZKListener zkListener) {
        subscriberNodeChangeEvent = zkListener;
    }

    public void setEventSourceDataChangeEvent(ZKListener zkListener) {
        eventSourceDataChangeEvent = zkListener;
    }

    public void setEventSourceChildNodeChangeEvent(ZKListener zkListener) {
        eventSourceChildNodeChangeEvent = zkListener;
    }

    public String getConnectString() {
        return connectString;
    }

    public void setConnectString(String _connectStr) {
        connectString = _connectStr;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(int _sessionTimeout) {
        sessionTimeout = _sessionTimeout;
    }

    public String getSubscriberRootName() {
        return subscriberRootName;
    }

    public void setSubscriberRootName(String _subscriberRootName) {
        this.subscriberRootName = _subscriberRootName;
    }

    public String getEventSourceRootName() {
        return eventSourceRootName;
    }

    public void setEventSourceRootName(String _eventSourceRootName) {
        eventSourceRootName = _eventSourceRootName;
    }
}
