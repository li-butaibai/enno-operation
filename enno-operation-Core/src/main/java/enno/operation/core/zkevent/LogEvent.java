package enno.operation.core.zkevent;

import enno.operation.ZKListener.EventLogListener;
import enno.operation.dal.EventLogEntity;
import enno.operation.dal.EventsourceEntity;
import enno.operation.dal.SubscriberEntity;
import enno.operation.dal.hibernateUtil;
import enno.operation.zkmodel.EventLogData;
import org.hibernate.Query;
import org.hibernate.Session;
import java.util.List;

/**
 * Created by v-zoli on 2015/11/10.
 */
public class LogEvent implements EventLogListener{
    public void process(List<EventLogData> eventLogDataList) {
        Session session = null;
        try {
            session = hibernateUtil.getSessionFactory().openSession();
            for (EventLogData eventLogData :eventLogDataList) {
                try {
                    EventLogEntity eventLogEntity = new EventLogEntity();
                    String esHql = "from EventsourceEntity as e fetch all properties where e.sourceId = :sourceId and e.dataStatus=0";
                    String sHql = "from SubscriberEntity as s fetch all properties where s.name=:subscriberName and s.dataStatus=0";
                    Query esQuery = session.createQuery(esHql);
                    Query sQuery = session.createQuery(sHql);
                    esQuery.setString("sourceId", eventLogData.getEventSourceId());
                    sQuery.setString("subscriberName", eventLogData.getSubscriberId());
                    EventsourceEntity eventsourceEntitiy = (EventsourceEntity)esQuery.uniqueResult();
                    SubscriberEntity subscriberEntity = (SubscriberEntity)sQuery.uniqueResult();
                    eventLogEntity.setSubscriber(subscriberEntity);
                    eventLogEntity.setEventsource(eventsourceEntitiy);
                    eventLogEntity.setLevel(eventLogData.getEventType().ordinal());
                    eventLogEntity.setTitle(eventLogData.getTitle());
                    session.save(eventLogEntity);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (session != null)
                session.close();
        }
    }
}
