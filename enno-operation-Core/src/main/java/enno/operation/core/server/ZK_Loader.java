package enno.operation.core.server;

import enno.operation.core.Operation.eventSourceOperation;
import enno.operation.core.zkevent.EventSourceEvent;
import enno.operation.core.zkevent.LogEvent;
import enno.operation.core.zkevent.SubscriberServerEvent;
import enno.operation.dal.EventsourceEntity;
import enno.operation.dal.hibernateUtil;
import enno.operation.zkl.ZKClient;
import enno.operation.zkmodel.EventSourceData;
import org.apache.zookeeper.ZooKeeper;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by v-zoli on 2015/11/27.
 */
public class ZK_Loader {
    private static ZKClient zkClient;
    private static Session session;
    private static EventSourceEvent eventSourceEvent = new EventSourceEvent();
    private static LogEvent logEvent = new LogEvent();
    private static SubscriberServerEvent subscriberServerEvent = new SubscriberServerEvent();
    public static void start() throws Exception{
        initializeZKClient();
        zkClient.connect();
        Map<String,String> eventSourceDatas = getAllEventSource();
        zkClient.initializeSchemaAndBeginTranscation(eventSourceDatas);
    }

    private static Map<String,String> getAllEventSource(){
        Map<String, String> dataList = new HashMap<String, String>();
        try {
            session = hibernateUtil.getSessionFactory().openSession();

            String queryHQL = "from EventsourceEntity es where es.dataStatus = 1";
            Query query = session.createQuery(queryHQL);
            List<EventsourceEntity> eventsourceEntityList = query.list();
            for (EventsourceEntity eventsourceEntity : eventsourceEntityList) {
                dataList.put(eventsourceEntity.getSourceId(), "");
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            session.close();
        }
        return dataList;
    }

    private static void initializeZKClient()
    {
        zkClient = ZKClient.getIntance();
        zkClient.setConnectString("127.0.0.1:2181");
        zkClient.setSessionTimeout(100000);
        zkClient.setSubscriberRootName("/SubscriberClusterRoot");
        zkClient.setEventLogRootName("/EventSourceRoot");
        zkClient.setEventLogRootName("/ConnectLogRoot");
        zkClient.setEventLogEvent(logEvent);
        zkClient.setEventSourceEvent(eventSourceEvent);
        zkClient.setSubscriberClusterEvent(subscriberServerEvent);

    }
}
