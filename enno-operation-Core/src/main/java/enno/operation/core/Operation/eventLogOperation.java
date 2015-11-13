package enno.operation.core.Operation;

import enno.operation.core.common.pageDivisionQueryUtil;
import enno.operation.core.model.EventLogModel;
import enno.operation.core.model.PageDivisionQueryResultModel;
import enno.operation.dal.EventLogEntity;
import enno.operation.dal.hibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sabermai on 2015/11/11.
 */
public class eventLogOperation {
    private Session session = null;
    private eventSourceOperation esOp = new eventSourceOperation();
    private subscriberOperation subOp = new subscriberOperation();

    public PageDivisionQueryResultModel<EventLogModel> getPageDivisionEventLogList(int pageIndex, int pageSize) throws Exception {
        PageDivisionQueryResultModel<EventLogModel> result = new PageDivisionQueryResultModel<EventLogModel>();
        try {
            PageDivisionQueryResultModel<EventLogEntity> entityQR = getPageDivisionEventLogEntityList(pageIndex, pageSize);
            result.setCurrentPageIndex(pageIndex);
            result.setRecordCount(entityQR.getRecordCount());
            result.setPageSize(pageSize);
            result.setQueryResult(ConvertEventlogList2ModelList(entityQR.getQueryResult()));
            return result;
        } catch (Exception ex) {
            throw ex;
        }
    }

    //获取指定EventSource的EventLog
    public List<EventLogModel> getEventLogsByEventsourceId(int EventsourceId) throws Exception {
        return ConvertEventlogList2ModelList(getEventLogListByEventsourceId(EventsourceId));
    }

    //获取指定Subscriber的EventLog
    public List<EventLogModel> getEventLogsBySubscriberId(int SubscriberId) throws Exception {
        return ConvertEventlogList2ModelList(getEventLogListBySubscriberId(SubscriberId));
    }

    //获取EventLog列表，分页
    private PageDivisionQueryResultModel<EventLogEntity> getPageDivisionEventLogEntityList(int pageIndex, int pageSize) throws Exception {
        pageDivisionQueryUtil<EventLogEntity> util = new pageDivisionQueryUtil();

        try {
            String queryHQL = "from EventLogEntity ee order by ee.createTime desc";
            String countHQL = "select count(*) from EventLogEntity ee";
            return util.excutePageDivisionQuery(pageIndex, pageSize, queryHQL, countHQL);
        } catch (Exception ex) {
            throw ex;
        }
    }

    //通过SubscriberId获取指定的Subscriber的日志
    private List<EventLogEntity> getEventLogListBySubscriberId(int SubscriberId) throws Exception {
        List<EventLogEntity> elList = null;

        try {
            session = hibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            Query q = session.createQuery("from EventLogEntity el where el.subscriber.id = :SubscriberId order by el.createTime desc");
            q.setParameter("SubscriberId", SubscriberId);
            elList = (List<EventLogEntity>) q.list();
            return elList;
        } catch (Exception ex) {
            throw ex;
        } finally {
            session.close();
        }
    }

    //通过EventsourceId获取指定的Eventsource的日志
    private List<EventLogEntity> getEventLogListByEventsourceId(int EventsourceId) throws Exception {
        List<EventLogEntity> elList = null;

        try {
            session = hibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            Query q = session.createQuery("from EventLogEntity el where el.eventsource.id = :EventsourceId order by el.createTime desc");
            q.setParameter("EventsourceId", EventsourceId);
            elList = (List<EventLogEntity>) q.list();
            return elList;
        } catch (Exception ex) {
            throw ex;
        } finally {
            session.close();
        }
    }

    private EventLogModel ConvertEventlogEntity2Model(EventLogEntity entity) throws Exception {
        EventLogModel log = new EventLogModel();
        try {
            log.setTitle(entity.getTitle());
            log.setId(entity.getId());
            log.setLevel(entity.getLevel());
            log.setMessage(entity.getMessage());
            log.setEventSourceModel(esOp.ConvertEventsourceEntity2Model(entity.getEventsource()));
            log.setSubscriberModel(subOp.ConvertSubscriberEntity2Model(entity.getSubscriber()));
            return log;
        } catch (Exception ex) {
            throw ex;
        }
    }

    private List<EventLogModel> ConvertEventlogList2ModelList(List<EventLogEntity> entities) throws Exception {
        List<EventLogModel> result = new ArrayList<EventLogModel>();
        for (EventLogEntity entity : entities) {
            EventLogModel log = new EventLogModel();
            log = ConvertEventlogEntity2Model(entity);
            result.add(log);
        }
        return result;
    }
}
