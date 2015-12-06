package enno.operation.zkl;

import enno.operation.ZKListener.SubscriptionListener;
import enno.operation.zkException.InitializeSchemaFailedException;
import enno.operation.zkmodel.EventLogData;
import enno.operation.zkmodel.EventSourceConnectModel;
import enno.operation.zkmodel.EventType;
import enno.operation.zkmodel.ZKSource;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.zookeeper.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by sabermai on 2015/12/1.
 */
public class ZKAgent {
    private String subscriberId = null;
    private String comments = null;
    private String ennoNodePath = null;
    private ZooKeeper zooKeeper = null;
    private ZKSource zkSource;
    private SubscriptionListener subscriptionEvent = null;
    public static List<EventSourceConnectModel> subscriberData = new ArrayList<EventSourceConnectModel>();

    public void setSubscriberId(String subscriberId) {
        this.subscriberId = subscriberId;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setZkSource(ZKSource zkSource) {
        this.zkSource = zkSource;
    }

    public void setSubscriptionEvent(SubscriptionListener subscriptionEvent) {
        this.subscriptionEvent = subscriptionEvent;
    }

    public ZKAgent() {
    }

    public void connect() {
        try {
            zooKeeper = new ZooKeeper(zkSource.getConnectString(), zkSource.getSessionTimeout(), null);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void close() {
        try {
            if (zooKeeper != null) {
                zooKeeper.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void initializeZKAgent() throws Exception {
        try {
            //System.out.println("Start initialize the zookeeper agent, event source id: " + subscriberId);
            ennoNodePath = zkSource.getSubscriberRootName() + "/" + subscriberId;

            //initialize the enno cluster root node
            if (zooKeeper.exists(zkSource.getSubscriberRootName(), false) == null) {
                zooKeeper.create(zkSource.getSubscriberRootName(), zkSource.getSubscriberRootName().getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }

            //initialize the event log root node
            if (zooKeeper.exists(zkSource.getEventLogRootName(), false) == null) {
                zooKeeper.create(zkSource.getEventLogRootName(), zkSource.getEventLogRootName().getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            }

            // check the enno cluster root children include current enno server(specified by the argument subscriberId)
            if (zooKeeper.exists(ennoNodePath, false) == null) {
                zooKeeper.create(ennoNodePath, comments.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                //System.out.println("Created the enno server node under SubscriberClusterRoot node.");
            }

            //watch the enno server node
            zooKeeper.getData(ennoNodePath, new Watcher() {
                public void process(WatchedEvent event) {
                    try {
                        String nodeData = new String(zooKeeper.getData(ennoNodePath, this, null));
                        JSONArray jsonArray = JSONArray.fromObject(nodeData);
                        List<EventSourceConnectModel> connectModelList = (List<EventSourceConnectModel>) JSONArray.toCollection(jsonArray, EventSourceConnectModel.class);
                        subscriberData = subscriptionEvent.process(subscriberId, connectModelList, subscriberData);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void CreateSubscriberNodeUnderEventSource(String subId, String eventSourceName) throws KeeperException, InterruptedException {
        if (zooKeeper.exists(zkSource.getEventSourceRootName() + "/" + eventSourceName + "/" + subId, false) == null) {
            zooKeeper.create(zkSource.getEventSourceRootName() + "/" + eventSourceName + "/" + subId, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        }
    }

    public void DeleteSubscriberNodeUnderEventSource(String subId, String eventSourceName) throws KeeperException, InterruptedException {
        if (zooKeeper.exists(zkSource.getEventSourceRootName() + "/" + eventSourceName + "/" + subId, false) != null) {
            zooKeeper.delete(zkSource.getEventSourceRootName() + "/" + eventSourceName + "/" + subId, -1);
        }
    }

    public void CreateSubscribeConnectLogNode(String subId, String eventSourceId, boolean isSuccess) throws KeeperException, InterruptedException {
        byte[] logContent;
        if (isSuccess) {
            logContent = (subId + " successfully subscribed the event source -- id: " + eventSourceId).getBytes();
        } else {
            logContent = (subId + " failed subscribed the event source -- id: " + eventSourceId).getBytes();
        }
        EventLogData eventLogData = generateEventLogData(subId, eventSourceId, isSuccess, "Create Subscription", new String(logContent));
        JSONObject jsonObject = JSONObject.fromObject(eventLogData);
        zooKeeper.create(zkSource.getEventLogRootName() + "/ConnectLog", jsonObject.toString().getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
    }

    public void CreateUnsubscribeConnectLogNode(String subId, String eventSourceId, boolean isSuccess) throws KeeperException, InterruptedException {
        byte[] logContent;
        if (isSuccess) {
            logContent = (subId + " successfully unsubscribed the event source -- id: " + eventSourceId).getBytes();
        } else {
            logContent = (subId + " failed unsubscribed the event source -- id: " + eventSourceId).getBytes();
        }
        EventLogData eventLogData = generateEventLogData(subId, eventSourceId, isSuccess, "Release Subscription", new String(logContent));
        JSONObject jsonObject = JSONObject.fromObject(eventLogData);
        zooKeeper.create(zkSource.getEventLogRootName() + "/ConnectLog", jsonObject.toString().getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
    }

    private EventLogData generateEventLogData(String subId, String eventSourceid, boolean result, String title, String message) {
        EventLogData eventLogData = new EventLogData();
        eventLogData.setSubscriberId(subId);
        eventLogData.setEventSourceId(eventSourceid);
        eventLogData.setEventType(result ? EventType.Info : EventType.Error);
        eventLogData.setMessage(message);
        eventLogData.setTitle(title);
        return eventLogData;
    }
}
