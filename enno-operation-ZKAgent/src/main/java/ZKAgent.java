import ZKAInterface.IProcessSubscriberData;
import ZKAInterface.ProcessSubscriberData;
import enno.operation.zkmodel.EventSourceConnectModel;
import enno.operation.zkmodel.ZKSource;
import net.sf.json.JSONArray;
import org.apache.zookeeper.*;

import java.util.*;

/**
 * Created by sabermai on 2015/11/30.
 */
public class ZKAgent {
    public ZKAgent(String subscriberId) {
        this.subscriberId = subscriberId;
        this.ennoNodePath = zkSource.getSubscriberRootName() + "/" + subscriberId;
    }

    private String subscriberId = null;
    private String ennoNodePath = null;
    private ZooKeeper zooKeeper = null;
    private ZKSource zkSource;
    public static List<EventSourceConnectModel> subscriberData = new ArrayList<EventSourceConnectModel>();

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
            final IProcessSubscriberData iProcessData = new ProcessSubscriberData();
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
            }

            //watch the enno server node
            zooKeeper.getData(ennoNodePath, new Watcher() {
                public void process(WatchedEvent event) {
                    try {
                        String nodeData = zooKeeper.getData(ennoNodePath, this, null).toString();
                        JSONArray jsonArray = JSONArray.fromObject(nodeData);
                        List<EventSourceConnectModel> connectModelList = (List<EventSourceConnectModel>) JSONArray.toCollection(jsonArray, EventSourceConnectModel.class);

                        //compare new data to old data
                        List<EventSourceConnectModel> addData = new ArrayList<EventSourceConnectModel>();
                        List<EventSourceConnectModel> removeData = new ArrayList<EventSourceConnectModel>();
                        Collections.copy(addData, connectModelList);
                        Collections.copy(removeData, subscriberData);
                        addData.removeAll(subscriberData);
                        removeData.removeAll(connectModelList);
                        //call enno server to add or remove subscription, return the process result
                        Map<String, List<EventSourceConnectModel>> map = iProcessData.processSubscriptions(subscriberId, addData, removeData);

                        //add and remove enno node under eventsource node
                        List<EventSourceConnectModel> addEventsources = map.get("add");
                        List<EventSourceConnectModel> removeEventsources = map.get("remove");

                        subscriberData.addAll(addData);
                        subscriberData.removeAll(removeData);

                        for (EventSourceConnectModel es : addEventsources) {
                            if (zooKeeper.exists(zkSource.getEventSourceRootName() + "/" + es.getSourceId() + "/" + subscriberId, false) == null) {
                                zooKeeper.create(zkSource.getEventSourceRootName() + "/" + es.getSourceId() + "/" + subscriberId, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                            }
                            byte[] logContent = (subscriberId + "successfully subscribed the event source named " + es.getSourceId()).getBytes();
                            zooKeeper.create(zkSource.getEventLogRootName() + "/ConnectLog", logContent, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
                        }
                        for (EventSourceConnectModel es : removeEventsources) {
                            if (zooKeeper.exists(zkSource.getEventSourceRootName() + "/" + es.getSourceId() + "/" + subscriberId, false) != null) {
                                zooKeeper.delete(zkSource.getEventSourceRootName() + "/" + es.getSourceId() + "/" + subscriberId, -1);
                            }
                            byte[] logContent = (subscriberId + "successfully unsubscribed the event source named " + es.getSourceId()).getBytes();
                            zooKeeper.create(zkSource.getEventLogRootName() + "/ConnectLog", logContent, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT_SEQUENTIAL);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
