package enno.operation.core.zkevent;

import enno.operation.ZKListener.EventLogListener;
import enno.operation.zkmodel.EventLogData;
import org.hibernate.Query;
import org.hibernate.Session;
import java.util.List;

/**
 * Created by v-zoli on 2015/11/10.
 */
public class LogEvent implements EventLogListener{
    public void process(List<EventLogData> eventLogDataList) {
//        Session session = null;
//        try {
//            session = hibernateHelper.getSessionFactory().openSession();
//            for (EventLogData eventLogData :eventLogDataList) {
//                try {
//                    EventLogEntity eventLogEntity = new EventLogEntity();
//                    String esHql = "from EventsourceEntity as e fetch all properties where e.sourceId = :sourceId and e.dataStatus=0";
//                    Query esQuery = session.createQuery(esHql);
//                    esQuery.setString("sourceId", eventLogData.getEventSourceId());
//                    List<EventsourceEntity> eventsourceEntities = esQuery.list();
////                    eventLogEntity.setEventSourceId(eventLogData.getEventSourceId());
////                    eventLogEntity.setSubscriberId(eventLogData.getSubscriberId());
//                    //TODO£ºeventLogEntity.setLevel(eventLogData.getLevel());
//                    eventLogEntity.setTitle(eventLogData.getTitle());
//                    session.save(eventLogEntity);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        } finally {
//            if (session != null)
//                session.close();
//        }
    }
}
