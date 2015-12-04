package enno.operation.core.zkevent;

import enno.operation.ZKListener.EventSourceListener;
import enno.operation.core.common.LogUtil;
import enno.operation.dal.EventsourceEntity;
import enno.operation.dal.EventsourceSubscriberMapEntity;
import enno.operation.dal.SubscriberEntity;
import enno.operation.dal.hibernateUtil;
import enno.operation.zkmodel.EventSourceData;
import enno.operation.zkmodel.SubscriberData;
import org.hibernate.Query;
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
            String eseHSQL ="from EventsourceEntity ese where ese.dataStatus=1 and ese.sourceId = :sourceId";
            Query eseQuery = session.createQuery(eseHSQL);
            eseQuery.setParameter("sourceId", eventSourceData.getEventSourceId());
            EventsourceEntity eventsourceEntity = (EventsourceEntity)eseQuery.uniqueResult();
//            EventsourceEntity eventsourceEntity
//                    = (EventsourceEntity)session.createQuery("from EventsourceEntity ese " +
//                    "where ese.dataStatus=1 and ese.sourceId = " + eventSourceData.getEventSourceId()).uniqueResult();
            String essmeHQL = "select esme from EventsourceSubscriberMapEntity esme right join esme.eventsource where esme.eventsource.sourceId = :sourceId";
            Query essmeQuery = session.createQuery(essmeHQL);
            essmeQuery.setParameter("sourceId", eventSourceData.getEventSourceId());

            List<EventsourceSubscriberMapEntity> eventsourceSubscriberMapEntities
                    = essmeQuery.list();
            for (EventsourceSubscriberMapEntity eventsourceSubscriberMapEntity : eventsourceSubscriberMapEntities) {
                session.delete(eventsourceSubscriberMapEntity);
            }
            for (SubscriberData subscriberData : eventSourceData.getSubscribers()) {
                String subHQL = "from SubscriberEntity se where se.dataStatus=1 and se.name  = :subName";
                Query subQuery = session.createQuery(subHQL);
                subQuery.setParameter("subName", subscriberData.getSubscriberId());
                SubscriberEntity subscriberEntity = (SubscriberEntity)subQuery.uniqueResult();
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
            //ex.printStackTrace();
            LogUtil.SaveLog(EventSourceEvent.class.getName(), ex);
        }
        finally {
            if (session != null)
                session.close();
        }
    }
}
