package enno.operation.core.Operation;

import enno.operation.core.model.EventSourceActivityModel;
import enno.operation.dal.hibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by sabermai on 2015/11/13.
 */
public class eventSourceActivityOpeartion {
    private Session session = null;

    //Done
    public List<EventSourceActivityModel> GetEventSourceActivityByEventsourceId(int EventsourceId, int EventsourceTemplateId) throws Exception {
        List<EventSourceActivityModel> activities = new ArrayList<EventSourceActivityModel>();
        try {
            session = hibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            Query q = session.createQuery("select ta.name,ta.comments,ta.displayName,ta.id,ae.value from EventsourceActivityEntity ae join ae.eventsourceTemplateActivity ta where ae.eventsource.id = :EventsourceId and ta.eventsourceTemplate.id = :EventsourceTemplateId");
            q.setParameter("EventsourceId", EventsourceId);
            q.setParameter("EventsourceTemplateId", EventsourceTemplateId);
            List list = q.list();
            Iterator it = list.iterator();
            while (it.hasNext()) {
                Object[] results = (Object[]) it.next();
                EventSourceActivityModel activity = new EventSourceActivityModel();
                activity.setName(results[0].toString());
                activity.setComments(results[1] != null ? results[1].toString() : null);
                activity.setDisplayName(results[2] != null ? results[2].toString() : null);
                activity.setTemplateActivityId((Integer) results[3]);
                activity.setValue(results[4] != null ? results[4].toString() : null);
                activities.add(activity);
            }
            return activities;
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (null != session) {
                session.close();
            }
        }
    }
}
