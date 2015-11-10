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
    private ZKListener ennoClusterNodeChangeEvent = null;
    private ZKListener eventSourceDataChangeEvent = null;
    private ZKListener eventSourceChildNodeChangeEvent = null;
    private String connectString = "";
    private int sessionTimeout = 1000;
    //    private long sessionId = 0;
//    private String sessionPasswd = "";
    private Map<String, String> eventSourceList = new HashMap<String, String>();
    private String ennoClusterRootName = "/EnnoClusterRoot";
    private String eventSourceRootName = "/EventSourceRoot";

    public ZKClient() {

    }

    public void start() {
        try {
            zooKeeper = new ZooKeeper(connectString, sessionTimeout, null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        initializeSchema();
    }
    private  void  processChildNodeAndData(List<String> nodes,ZKListener zkListener)
    {
        Map<String, String> ennoNodes =null;
        try {
            ennoNodes = new HashMap<String, String>();
            for (String node : nodes) {
                String nodeData = new String(zooKeeper.getData(node, false, null));
                ennoNodes.put(node, nodeData);
            }
            if (zkListener != null) {
                zkListener.process(ennoNodes);
            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    private void processNodeAndData(String node,String data, ZKListener zkListener)
    {
        Map<String, String> ennoNodes =null;
        try {
            ennoNodes = new HashMap<String, String>();
            ennoNodes.put(node, data);
            if (zkListener != null) {
                zkListener.process(ennoNodes);
            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    //initialize the zookeeper schema, and add the watch to the event
    private boolean initializeSchema() {
        try {
            //initialize the enno cluster root node
            if (zooKeeper.exists(ennoClusterRootName, false) == null) {
                zooKeeper.create(ennoClusterRootName, ennoClusterRootName.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            // watch the enno cluster root child node
            Map<String,String> ennoNodes = new HashMap<String, String>();
            List<String> ennoClusterChildrenNodes = zooKeeper.getChildren(ennoClusterRootName, new Watcher() {
                //@Override
                public void process(WatchedEvent event) {
                    try {
                        List<String> ennoClusterChildrenNodes = zooKeeper.getChildren(event.getPath(), this, null);
                        processChildNodeAndData(ennoClusterChildrenNodes,ennoClusterNodeChangeEvent);
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            }, null);
            processChildNodeAndData(ennoClusterChildrenNodes,ennoClusterNodeChangeEvent);
            //initialize the event source root node
            if (zooKeeper.exists(eventSourceRootName, false) == null) {
                zooKeeper.create(eventSourceRootName, eventSourceRootName.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            //initialize the sub event source node
            for (Map.Entry<String, String> eventSource : eventSourceList.entrySet()) {
                String path = eventSourceRootName + "/" + eventSource.getKey();
                //create the sub event source node
                if (zooKeeper.exists(path, false) == null) {
                    zooKeeper.create(path, eventSource.getValue().getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
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
                processNodeAndData(path,new String(nodeData), eventSourceDataChangeEvent);
                //watch the event source children node change event
                List<String> eventSourceChildrenNodes = zooKeeper.getChildren(path, new Watcher() {
                    //@Override
                    public void process(WatchedEvent event) {
                        try {
                            List<String> eventSourceChildrenNodes = zooKeeper.getChildren(event.getPath(), this, null);
                            processChildNodeAndData(eventSourceChildrenNodes,eventSourceChildNodeChangeEvent);
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                }, null);
                processChildNodeAndData(eventSourceChildrenNodes,eventSourceChildNodeChangeEvent);
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
                            processChildNodeAndData(eventSourceChildrenNodes,eventSourceChildNodeChangeEvent);
                        }catch (Exception ex){
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

    public void setEnnoData(String ennoName, String data)
    {
        try{
            String path = String.format("%s/%s", ennoClusterRootName, ennoName);
            if(zooKeeper.exists(path, false) != null) {
                zooKeeper.setData(path, data.getBytes(), -1);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public ZooKeeper getZooKeeper() {
        return zooKeeper;
    }

    public void setEnnoClusterNodeChangeEvent(ZKListener zkListener) {
        ennoClusterNodeChangeEvent = zkListener;
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

    public String getEnnoClusterRootName() {
        return ennoClusterRootName;
    }

    public void setEnnoClusterRootName(String _ennoClusterRootName) {
        this.ennoClusterRootName = _ennoClusterRootName;
    }

    public String getEventSourceRootName() {
        return eventSourceRootName;
    }

    public void setEventSourceRootName(String _eventSourceRootName) {
        eventSourceRootName = _eventSourceRootName;
    }

    public void setEventSourceList(Map<String, String> _eventSourceList) {
        eventSourceList = _eventSourceList;
    }

    public Map<String,String> getEventSourceList() {
        return eventSourceList;
    }
}
