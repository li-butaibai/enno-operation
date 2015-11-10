package Enno.Operation.ZKL;

import org.apache.zookeeper.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by EriclLee on 15/11/9.
 */
public class ZKClient {
    private ZooKeeper zooKeeper = null;
    private ZKListener subscriberNodeChangeEvent = null;
    private ZKListener eventSourceDataChangeEvent = null;
    private ZKListener eventSourceChildNodeChangeEvent = null;
    private String connectString = "";
    private int sessionTimeout = 1000;
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

    private void processChildNodeAndData(List<String> nodes, ZKListener zkListener) {
        Map<String, String> ennoNodes = null;
        try {
            ennoNodes = new HashMap<String, String>();
            for (String node : nodes) {
                String nodeData = new String(zooKeeper.getData(node, false, null));
                ennoNodes.put(node, nodeData);
            }
            if (zkListener != null) {
                zkListener.process(ennoNodes);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void processNodeAndData(String node, String data, ZKListener zkListener) {
        Map<String, String> ennoNodes = null;
        try {
            ennoNodes = new HashMap<String, String>();
            ennoNodes.put(node, data);
            if (zkListener != null) {
                zkListener.process(ennoNodes);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //initialize the zookeeper schema, and add the watch to the event
    private boolean initializeSchema(Map<String, String> eventSourceList) {
        try {
            //1. initialize the enno cluster root node
            boolean newsubscriberRoot = false;
            if (zooKeeper.exists(subscriberRootName, false) == null) {
                zooKeeper.create(subscriberRootName, subscriberRootName.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                newsubscriberRoot = true;
            }
            // watch the enno cluster root child node
            List<String> ennoClusterChildrenNodes = zooKeeper.getChildren(subscriberRootName, new Watcher() {
                //@Override
                public void process(WatchedEvent event) {
                    try {
                        List<String> ennoClusterChildrenNodes = zooKeeper.getChildren(event.getPath(), this, null);
                        processChildNodeAndData(ennoClusterChildrenNodes, subscriberNodeChangeEvent);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }, null);
            //已经存在需要同步数据
            if(newsubscriberRoot == false) {
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
                boolean newES = false;
                if (zooKeeper.exists(path, false) == null) {
                    zooKeeper.create(path, eventSource.getValue().getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                    newES=true;
                }
                //watch the event source node data change event
                byte[] nodeData = zooKeeper.getData(path, new Watcher() {
                    //@Override
                    public void process(WatchedEvent event) {
                        try {
                            String nodeData = new String(zooKeeper.getData(event.getPath(), this, null));
                            processNodeAndData(event.getPath(), nodeData, eventSourceDataChangeEvent);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }, null);
                if(newES==false) {
                    processNodeAndData(path, new String(nodeData), eventSourceDataChangeEvent);
                }
                //watch the event source children node change event
                List<String> eventSourceChildrenNodes = zooKeeper.getChildren(path, new Watcher() {
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
                processChildNodeAndData(eventSourceChildrenNodes, eventSourceChildNodeChangeEvent);
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    //add new event source to zookeeper
    public void addEventSource(String eventSourceName, String eventSourceData) {
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
                            processNodeAndData(event.getPath(), nodeData, eventSourceDataChangeEvent);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }, null);
                //watch the event source children node change event
                zooKeeper.getChildren(path, new Watcher() {
                    //@Override
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //remove event source from zookeeper
    public void removeEventSource(String eventSourceName) {
        try {
            String path = eventSourceRootName + "/" + eventSourceName;
            if (zooKeeper.exists(path, false) != null) {
                zooKeeper.delete(path, -1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setEnnoData(String ennoName, String data) {
        try {
            String path = String.format("%s/%s", subscriberRootName, ennoName);
            if (zooKeeper.exists(path, false) != null) {
                zooKeeper.setData(path, data.getBytes(), -1);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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

    public void setSubscriberRootName(String _SubscriberRootName) {
        this.subscriberRootName = _SubscriberRootName;
    }

    public String getEventSourceRootName() {
        return eventSourceRootName;
    }

    public void setEventSourceRootName(String _eventSourceRootName) {
        eventSourceRootName = _eventSourceRootName;
    }

    public Map<String, String> getEventSourceList() {
        Map<String, String> esList = new HashMap<String, String>();
        try {
            List<String> eventSourceChildrenNodes = zooKeeper.getChildren(eventSourceRootName, false);
            for(String esPath : eventSourceChildrenNodes)
            {
                String esData = new String(zooKeeper.getData(esPath, false, null));
                esList.put(esPath,esData);
            }
        }
        catch (Exception ex)
        {
            esList.clear();
            ex.printStackTrace();
        }
        return esList;
    }

    public Map<String, String> getSubscriberList() {
        Map<String, String> subscriberList = new HashMap<String, String>();
        try {
            List<String> subscriberChildrenNodes = zooKeeper.getChildren(subscriberRootName, false);
            for(String subscriberPath : subscriberChildrenNodes)
            {
                String subscriberData = new String(zooKeeper.getData(subscriberPath, false, null));
                subscriberList.put(subscriberPath,subscriberData);
            }
        }
        catch (Exception ex)
        {
            subscriberList.clear();
            ex.printStackTrace();
        }
        return subscriberList;
    }

    public void setSubscriberData(String subscriberName, String data)
    {
        try {
            zooKeeper.setData(subscriberRootName + "/" + subscriberName, data.getBytes(), -1);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void setEventSourceData(String sourceId, String data)
    {
        try {
            zooKeeper.setData(eventSourceRootName + "/" + sourceId, data.getBytes(), -1);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

}
