package enno.operation.core.Operation;

import enno.operation.core.common.pageDivisionQueryUtil;
import enno.operation.core.model.PageDivisionQueryResultModel;
import enno.operation.dal.EventsourceEntity;
import enno.operation.dal.hibernateHelper;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Created by sabermai on 2015/11/10.
 */
public class eventSourceOperation {
    private Session session = null;

    public eventSourceOperation(){
        this.session = hibernateHelper.getSessionFactory().openSession();
    }

    //获取EventSource列表，分页
    public PageDivisionQueryResultModel<EventsourceEntity> getEventSourceList(int pageIndex, int pageSize) throws Exception {
        pageDivisionQueryUtil<EventsourceEntity> util = new pageDivisionQueryUtil();

        try {
            String queryHQL = "from EventsourceEntity es where es.status = 1 and es.dataStatus = 1 order by es.id asc";
            String countHQL = "select count(*) from EventsourceEntity es where es.status = 1 and es.dataStatus = 1";
            return util.excutePageDivisionQuery(pageIndex,pageSize,queryHQL,countHQL);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    //通过Id获取指定的EventSource
    public EventsourceEntity getEventSourceById(int Id) throws Exception{
        EventsourceEntity es = new EventsourceEntity();

        try {
            Transaction tx = session.beginTransaction();
            Query q = session.createQuery("from EventsourceEntity es where es.id = :EventsourceId");
            q.setParameter("EventsourceId", Id);
            es = (EventsourceEntity)q.uniqueResult();
            return es;
        }
        catch (Exception ex){
            throw ex;
        }
        finally {
            session.close();
        }
    }
}
