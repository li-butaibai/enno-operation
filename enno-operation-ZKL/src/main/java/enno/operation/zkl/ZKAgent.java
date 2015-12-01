package enno.operation.zkl;

import enno.operation.ZKListener.SubscriptionListener;
import enno.operation.zkException.InitializeSchemaFailedException;
import enno.operation.zkmodel.EventSourceConnectModel;
import enno.operation.zkmodel.ZKSource;
import net.sf.json.JSONArray;
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
    private String ennoNodePath = null;
    private ZooKeeper zooKeeper = null;
    private ZKSource zkSource;
    private SubscriptionListener subscriptionEvent = null;
    public static List<EventSourceConnectModel> subscriberData = new ArrayList<EventSourceConnectModel>();

    public void setSubscriberId(String subscriberId) {
        this.subscriberId = subscriberId;
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
            System.out.println("Start initialize the zookeeper agent, event source id: " + subscriberId);

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
                zooKeeper.create(ennoNodePath, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                System.out.println("Created the enno server node under EventsourceRoot node.");
            }

            //watch the enno server node
            zooKeeper.getData(ennoNodePath, new Watcher() {
                public void process(WatchedEvent event) {
                    try {
                        String nodeData = zooKeeper.getData(ennoNodePath, this, null).toString();
                        JSONArray jsonArray = JSONArray.fromObject(nodeData);
                        List<EventSourceConnectModel> connectModelList = (List<EventSourceConnectModel>) JSONArray.toCollection(jsonArray, EventSourceConnectModel.class);
                        processSubscription(connectModelList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processSubscription(List<EventSourceConnectModel> connectModelList) throws KeeperException, InterruptedException {
        //compare new data to old data
        List<EventSourceConnectModel> addData = new ArrayList<EventSourceConnectModel>();
        List<EventSourceConnectModel> removeData = new ArrayList<EventSourceConnectModel>();
        Collections.copy(addData, connectModelList);
        Collections.copy(removeData, subscriberData);
        addData.removeAll(subscriberData);
        removeData.removeAll(connectModelList);
        //call enno server to add or remove subscription, return the process result
        Map<String, List<EventSourceConnectModel>> map = subscriptionEvent.process(subscriberId, addData, removeData);

        //add and remove enno node under eventsource node
        List<EventSourceConnectModel> addEventsources = map.get("add");
        List<EventSourceConnectModel> removeEventsources = map.get("remove");

        subscriberData.addAll(addData);
        subscriberData.removeAll(removeData);

        for (EventSourceConnectModel es : addEventsources) {
            if (zooKeeper.exists(zkSource.getEventSourceRootName() + "/" + es.getSourceId() + "/" + subscriberId, false) == null) {
                zooKeeper.create(zkSource.getEventSourceRootName() + "/" + es.getSourceId() + "/" + subscriberId, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                System.out.println("Created the enno server node under event source node named " + es.getSourceId());
            }
            byte[] logContent = (subscriberId + "successfully subscribed the event source named " + es.getSourceId()).getBytes();
            zooKeeper.create(zkSource.getEventLogRootName() + "/ConnectLog", logContent, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
        }
        for (EventSourceConnectModel es : removeEventsources) {
            if (zooKeeper.exists(zkSource.getEventSourceRootName() + "/" + es.getSourceId() + "/" + subscriberId, false) != null) {
                zooKeeper.delete(zkSource.getEventSourceRootName() + "/" + es.getSourceId() + "/" + subscriberId, -1);
                System.out.println("Removed the enno server node under event source node named " + es.getSourceId());
            }
            byte[] logContent = (subscriberId + "successfully unsubscribed the event source named " + es.getSourceId()).getBytes();
            zooKeeper.create(zkSource.getEventLogRootName() + "/ConnectLog", logContent, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
        }
    }
}
