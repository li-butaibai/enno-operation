package enno.operation.core.Operation;

import enno.operation.core.common.pageDivisionQueryUtil;
import enno.operation.core.model.PageDivisionQueryResultModel;
import enno.operation.dal.hibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * Created by sabermai on 2015/11/11.
 */
public class eventLogOperation {
    private Session session = null;

    public eventLogOperation(){
        this.session = hibernateUtil.getSessionFactory().openSession();
    }

    //获取EventLog列表，分页
    public PageDivisionQueryResultModel<EventLogEntity> getEventLogList(int pageIndex, int pageSize) throws Exception {
        pageDivisionQueryUtil<EventLogEntity> util = new pageDivisionQueryUtil();

        try {
            String queryHQL = "from EventLogEntity ee order by ee.createTime desc";
            String countHQL = "select count(*) from EventLogEntity ee";
            return util.excutePageDivisionQuery(pageIndex,pageSize,queryHQL,countHQL);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    //通过SubscriberId获取指定的Subscriber的日志
    public List<EventLogEntity> getEventLogListBySubscriberId(int SubscriberId) throws Exception{
        List<EventLogEntity> elList = null;

        try {
            Transaction tx = session.beginTransaction();
            Query q = session.createQuery("from EventLogEntity el where el.subscriberId = :SubscriberId");
            q.setParameter("SubscriberId", SubscriberId);
            elList = (List<EventLogEntity>)q.list();
            return elList;
        }
        catch (Exception ex){
            throw ex;
        }
        finally {
            session.close();
        }
    }

    //通过EventsourceId获取指定的Eventsource的日志
    public List<EventLogEntity> getEventLogListByEventsourceId(int EventsourceId) throws Exception{
        List<EventLogEntity> elList = null;

        try {
            Transaction tx = session.beginTransaction();
            Query q = session.createQuery("from EventLogEntity el where el.eventSourceId = :EventsourceId");
            q.setParameter("EventsourceId", EventsourceId);
            elList = (List<EventLogEntity>)q.list();
            return elList;
        }
        catch (Exception ex){
            throw ex;
        }
        finally {
            session.close();
        }
    }
}
