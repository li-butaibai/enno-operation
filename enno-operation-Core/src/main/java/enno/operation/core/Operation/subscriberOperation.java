package enno.operation.core.Operation;

import enno.operation.core.common.pageDivisionQueryUtil;
import enno.operation.core.model.PageDivisionQueryResultModel;
import enno.operation.dal.hibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Created by sabermai on 2015/11/11.
 */
public class subscriberOperation {
    private Session session = null;

    public subscriberOperation(){
        this.session = hibernateUtil.getSessionFactory().openSession();
    }

    //获取Subscriber列表，分页
    public PageDivisionQueryResultModel<SubscriberEntity> getSubscriberList(int pageIndex, int pageSize) throws Exception {
        pageDivisionQueryUtil<SubscriberEntity> util = new pageDivisionQueryUtil();

        try {
            String queryHQL = "from SubscriberEntity se where se.status = 1 and se.dataStatus = 1 order by se.id asc";
            String countHQL = "select count(*) from SubscriberEntity se where se.status = 1 and se.dataStatus = 1";
            return util.excutePageDivisionQuery(pageIndex,pageSize,queryHQL,countHQL);
        }
        catch (Exception ex) {
            throw ex;
        }
    }

    //通过Id获取指定的Subscriber
    public SubscriberEntity getSubscriberById(int Id) throws Exception{
        SubscriberEntity se = new SubscriberEntity();

        try {
            Transaction tx = session.beginTransaction();
            Query q = session.createQuery("from SubscriberEntity se where se.id = :SubscriberId");
            q.setParameter("SubscriberId", Id);
            se = (SubscriberEntity)q.uniqueResult();
            return se;
        }
        catch (Exception ex){
            throw ex;
        }
        finally {
            session.close();
        }
    }
}
