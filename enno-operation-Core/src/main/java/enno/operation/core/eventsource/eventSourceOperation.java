package enno.operation.core.eventsource;

import enno.operation.dal.EventsourceEntity;
import enno.operation.dal.hibernateHelper;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

/**
 * Created by sabermai on 2015/11/10.
 */
public class eventSourceOperation {
    private Session session = null;

    public eventSourceOperation(){
        this.session = hibernateHelper.getSessionFactory().openSession();
    }

    //获取EventSource列表
    public List<EventsourceEntity> getEventSourceList() throws Exception {
        List<EventsourceEntity> esList = null;

        try {
            Transaction tx = session.beginTransaction();
            Query q = session.createQuery("");
            esList = (List<EventsourceEntity>)q.list();
            return esList;
        }
        catch (Exception ex){
            throw ex;
        }
    }

    public EventsourceEntity getEventSourceById(int Id) throws Exception{
        List<EventsourceEntity> esList = null;

        try {
            Transaction tx = session.beginTransaction();
            Query q = session.createQuery("");
            esList = (List<EventsourceEntity>)q.list();
            return esList.get(0);
        }
        catch (Exception ex){
            throw ex;
        }
    }
}
