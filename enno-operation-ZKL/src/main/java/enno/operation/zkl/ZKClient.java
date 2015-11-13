package enno.operation.zkl;

import enno.operation.ZKListener.EventLogListener;
import enno.operation.ZKListener.EventSourceListener;
import enno.operation.ZKListener.SubscriberClusterListener;
import enno.operation.zkException.InitializeSchemaFailedException;
import enno.operation.zkmodel.EventLogData;
import enno.operation.zkmodel.EventSourceConnectModel;
import enno.operation.zkmodel.EventSourceData;
import enno.operation.zkmodel.SubscriberData;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import org.apache.zookeeper.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by v-zoli on 2015/10/22.
 */
public class ZKClient {
    private static class ZKClientHolder{
        private static final ZKClient INSTANCE=new ZKClient();
    }
    private ZKClient() {}
    public  static final ZKClient getIntance(){
        return ZKClientHolder.INSTANCE;
    }
    private ZooKeeper zooKeeper = null;
    private SubscriberClusterListener subscriberClusterEvent = null;
    private EventLogListener eventLogEvent = null;
    private EventSourceListener eventSourceEvent = null;
    private String connectString = "";
    private int sessionTimeout = 1000;
    private String subscriberRootName = "/SubscriberClusterRoot";
    private String eventSourceRootName = "/EventSourceRoot";
    private String eventLogRootName = "/EventLogRoot";

    public void connect() {
        try {
            zooKeeper = new ZooKeeper(connectString, sessionTimeout, null);
    } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void close(){
        try {
            if(zooKeeper!=null) {
                zooKeeper.close();
            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void processSubscriberList(List<String> nodes)
    {
        try {
            List<SubscriberData> subscriberDataList = new ArrayList<SubscriberData>();
            for (String node : nodes) {
                SubscriberData subscriberData = new SubscriberData();
                subscriberData.setSubscriberId(node);
                String nodeData = new String(zooKeeper.getData(subscriberRootName + "/" + node, false, null));
                subscriberData.setSubscriberData(nodeData);
                subscriberDataList.add(subscriberData);
            }
            if(subscriberClusterEvent != null)
            {
                subscriberClusterEvent.process(subscriberDataList);
            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void processEventSource(String parentNode, List<String> nodes)
    {
        try {
            EventSourceData eventSourceData = new EventSourceData();
            eventSourceData.setEventSourceId(parentNode);
            List<SubscriberData> subscriberDataList = new ArrayList<SubscriberData>();
            for (String node : nodes) {
                SubscriberData subscriberData = new SubscriberData();
                subscriberData.setSubscriberId(node);
                String nodeData = new String(zooKeeper.getData(subscriberRootName + "/" + node, false, null));
                subscriberData.setSubscriberData(nodeData);
                subscriberDataList.add(subscriberData);
            }
            eventSourceData.setSubscribers(subscriberDataList);
            if(eventSourceEvent != null)
            {
                eventSourceEvent.process(eventSourceData);
            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    private void processEventLog(List<String> nodes)
    {
        try {
            List<EventLogData> eventLogDataList = new ArrayList<EventLogData>();
            for (String node : nodes) {
                String nodeData = new String(zooKeeper.getData(eventLogRootName + "/" + node, false, null));
                JSONObject jsonObject = JSONObject.fromObject(nodeData);
                EventLogData eventLogData =(EventLogData)JSONObject.toBean(jsonObject, EventLogData.class);
                eventLogDataList.add(eventLogData);
            }
            if(eventLogEvent != null)
            {
                eventLogEvent.process(eventLogDataList);
            }
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    //initialize the zookeeper schema, and add the watch to the event
    public void initializeSchemaAndBeginTranscation(Map<String, String> eventSourceList) throws InitializeSchemaFailedException {
        try {
            //initialize the enno cluster root node
            boolean newSubscriberRoot = false;
            if (zooKeeper.exists(subscriberRootName, false) == null) {
                zooKeeper.create(subscriberRootName, subscriberRootName.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                newSubscriberRoot = true;
            }
            // watch the enno cluster root child node
            List<String> clusterChildrenNodes = zooKeeper.getChildren(subscriberRootName, new Watcher() {
                //@Override
                public void process(WatchedEvent event) {
                    try {
                        List<String> clusterChildrenNodes = zooKeeper.getChildren(event.getPath(), this, null);
                        processSubscriberList(clusterChildrenNodes);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }, null);
            if (newSubscriberRoot == false) {
                processSubscriberList(clusterChildrenNodes);
            }

            //initialize the event source root node
            if (zooKeeper.exists(eventSourceRootName, false) == null) {
                zooKeeper.create(eventSourceRootName, eventSourceRootName.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            //initialize the sub event source node
            for (final Map.Entry<String, String> eventSource : eventSourceList.entrySet()) {
                String path = eventSourceRootName + "/" + eventSource.getKey();
                //create the sub event source node
                boolean newEventSource = false;
                if (zooKeeper.exists(path, false) == null) {
                    zooKeeper.create(path, eventSource.getValue().getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                    newEventSource = true;
                }
                //watch the event source children node change event
                List<String> eventSourceChildrenNodes = zooKeeper.getChildren(path, new Watcher() {
                    //@Override
                    public void process(WatchedEvent event) {
                        try {
                            List<String> eventSourceChildrenNodes = zooKeeper.getChildren(event.getPath(), this, null);
                            processEventSource(eventSource.getKey(), eventSourceChildrenNodes);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }, null);
                processEventSource(eventSource.getKey(), eventSourceChildrenNodes);
            }

            //initialize the event log root node
            if (zooKeeper.exists(eventLogRootName, false) == null) {
                zooKeeper.create(eventLogRootName, eventLogRootName.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }
            zooKeeper.getChildren(eventLogRootName, new Watcher() {
                //@Override
                public void process(WatchedEvent event) {
                    try {
                        List<String> eventSourceChildrenNodes = zooKeeper.getChildren(event.getPath(), this, null);
                        processEventLog(eventSourceChildrenNodes);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }, null);
        }
        catch (Exception ex)
        {
            throw new InitializeSchemaFailedException(ex.getMessage(), ex.getCause());
        }
    }

    //add new event source to zookeeper
    public void addEventSource(String eventSourceName, final String eventSourceData)
    {
        try {
            String path = eventSourceRootName + "/" + eventSourceName;
            if (zooKeeper.exists(path, false) == null) {
                zooKeeper.create(path, eventSourceData.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                //watch the event source children node change event
                zooKeeper.getChildren(path, new Watcher() {
                    //@Override
                    public void process(WatchedEvent event) {
                        try {
                            List<String> eventSourceChildrenNodes = zooKeeper.getChildren(event.getPath(), this, null);
                            processEventSource(eventSourceData, eventSourceChildrenNodes);
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

    public void setSubscriberData(String subscriberId, List<EventSourceConnectModel> connectModelList)
    {
        try{
            String data = JSONArray.fromObject(connectModelList).toString();
            String path = String.format("%s/%s", subscriberRootName, subscriberId);
            if(zooKeeper.exists(path, false) != null) {
                zooKeeper.setData(path, data.toString().getBytes(), -1);
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

    public void setSubscriberClusterEvent(SubscriberClusterListener zkListener) {
        subscriberClusterEvent = zkListener;
    }

    public void setEventLogEvent(EventLogListener zkListener) {
         eventLogEvent= zkListener;
    }

    public void setEventSourceEvent(EventSourceListener zkListener) {
        eventSourceEvent = zkListener;
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

    public String getEventLogRootName() {
        return eventLogRootName;
    }

    public void setEventLogRootName(String eventLogRootName) {
        this.eventLogRootName = eventLogRootName;
    }
}
