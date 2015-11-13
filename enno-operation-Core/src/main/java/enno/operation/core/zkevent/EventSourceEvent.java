package enno.operation.core.zkevent;

import enno.operation.ZKListener.EventSourceListener;
import enno.operation.dal.EventsourceEntity;
import enno.operation.dal.EventsourceSubscriberMapEntity;
import enno.operation.dal.SubscriberEntity;
import enno.operation.dal.hibernateUtil;
import enno.operation.zkmodel.EventSourceData;
import enno.operation.zkmodel.SubscriberData;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * Created by v-zoli on 2015/11/13.
 */
public class EventSourceEvent implements EventSourceListener {
    public void process(EventSourceData eventSourceData) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = hibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            EventsourceEntity eventsourceEntity
                    = (EventsourceEntity)session.createQuery("from EventsourceEntity ese " +
                    "where ese.dataStatus=0 and ese.sourceId = " + eventSourceData.getEventSourceId()).uniqueResult();
            List<EventsourceSubscriberMapEntity> eventsourceSubscriberMapEntities
                    = session.createQuery("from EventsourceSubscriberMapEntity esme right join esme.eventsource " +
                    "where esme.eventsource.sourceId = " + eventSourceData.getEventSourceId()).list();
            for (EventsourceSubscriberMapEntity eventsourceSubscriberMapEntity : eventsourceSubscriberMapEntities) {
                session.delete(eventsourceSubscriberMapEntity);
            }
            for (SubscriberData subscriberData : eventSourceData.getSubscribers()) {
                SubscriberEntity subscriberEntity = (SubscriberEntity)session.createQuery("from SubscriberEntity se " +
                        "where se.dataStatus=0 and se.name = " + subscriberData.getSubscriberId()).uniqueResult();
                EventsourceSubscriberMapEntity eventsourceSubscriberMapEntity = new EventsourceSubscriberMapEntity();
                eventsourceSubscriberMapEntity.setEventsource(eventsourceEntity);
                eventsourceSubscriberMapEntity.setSubscriber(subscriberEntity);
                session.save(eventsourceSubscriberMapEntity);
            }
            transaction.commit();
        }catch (Exception ex)
        {
            if(transaction!=null)
            {
                transaction.rollback();
            }
            ex.printStackTrace();
        }
        finally {
            if (session != null)
                session.close();
        }
    }
}
