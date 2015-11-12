package enno.operation.core.zkevent;

import enno.operation.core.DataConversion.DataConversionFactory;
import enno.operation.core.DataConversion.EventSourceDataConversion;
import enno.operation.core.DataConversion.IDataConversion;
import enno.operation.core.model.EventSourceModel;
import enno.operation.core.model.SubscriberModel;
import enno.operation.dal.*;
import enno.operation.zkl.ZKListener;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Map;

/**
 * Created by v-zoli on 2015/11/10.
 */
public class eventSourceDataChangeEvent implements ZKListener {
    public void process(Map<String, String> nodes) {
        Session session = null;
        try {
            session = hibernateUtil.getSessionFactory().openSession();
            for (Map.Entry<String, String> node : nodes.entrySet()) {
                Transaction transaction = null;
                try {
                    transaction = session.beginTransaction();
                    String path = node.getKey();
                    String data = node.getValue();
                    DataConversionFactory<EventSourceModel> dataConversionFactory = new DataConversionFactory<EventSourceModel>();
                    IDataConversion<EventSourceModel> dataConversion = dataConversionFactory.createDataConversion(EventSourceDataConversion.class);
                    EventSourceModel eventSourceModel = dataConversion.Decode(path, data);
                    List<EventsourceSubscriberMapEntity> eventsourceSubscriberMapEntities
                            = session.createQuery("from EventsourceSubscriberMapEntity fetch all properties where eventSourceId= " + eventSourceModel.getId()).list();
                    for (EventsourceSubscriberMapEntity eventsourceSubscriberMapEntity : eventsourceSubscriberMapEntities) {
                        session.delete(eventsourceSubscriberMapEntity);
                    }
                    for (SubscriberModel subscriberModel : eventSourceModel.getSubscriberList()) {
                        EventsourceSubscriberMapEntity eventsourceSubscriberMapEntity = new EventsourceSubscriberMapEntity();
                        eventsourceSubscriberMapEntity.setEventSourceId(eventSourceModel.getId());
                        eventsourceSubscriberMapEntity.setSubscriberId(subscriberModel.getId());
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
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (session != null)
                session.close();
        }
    }
}
