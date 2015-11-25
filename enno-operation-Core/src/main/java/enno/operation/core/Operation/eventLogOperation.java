package enno.operation.core.Operation;

import enno.operation.core.common.LogUtil;
import enno.operation.core.common.pageDivisionQueryUtil;
import enno.operation.core.model.*;
import enno.operation.core.model.Enum;
import enno.operation.dal.EventLogEntity;
import enno.operation.dal.hibernateUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
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
    private eventSourceOperation esOp = null;
    private subscriberOperation subOp = null;
    private static Logger logger = LogManager.getLogger(eventLogOperation.class.getName());

    public PageDivisionQueryResultModel<EventLogModel> getPageDivisionEventLogList(int pageIndex) throws Exception {
        PageDivisionQueryResultModel<EventLogModel> result = new PageDivisionQueryResultModel<EventLogModel>();
        try {
            PageDivisionQueryResultModel<EventLogEntity> entityQR = getPageDivisionEventLogEntityList(pageIndex);
            result.setCurrentPageIndex(pageIndex);
            result.setRecordCount(entityQR.getRecordCount());
            //result.setPageSize(pageSize);
            result.setQueryResult(ConvertEventlogList2ModelList(entityQR.getQueryResult()));
            result.setPageCount();
            return result;
        } catch (Exception ex) {
            LogUtil.SaveLog(eventLogOperation.class.getName(), ex);
            throw ex;
        }
    }

    //获取指定EventSource的EventLog
    public List<EventLogModel> getEventLogsByEventsourceId(int EventsourceId, int Count) throws Exception {
        return ConvertEventlogList2ModelList(getEventLogListByEventsourceId(EventsourceId, Count));
    }

    //获取指定Subscriber的EventLog
    public List<EventLogModel> getEventLogsBySubscriberId(int SubscriberId, int Count) throws Exception {
        return ConvertEventlogList2ModelList(getEventLogListBySubscriberId(SubscriberId, Count));
    }

    public EventLogModel getEventLogById(int EventLogId) throws Exception {
        try {
            session = hibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            Query q = session.createQuery("from EventLogEntity log where log.id = :LogId");
            q.setParameter("LogId", EventLogId);
            EventLogEntity logEntity = (EventLogEntity) q.uniqueResult();
            return ConvertEventlogEntity2Model(logEntity);
        } catch (Exception ex) {
            LogUtil.SaveLog(eventLogOperation.class.getName(), ex);
            throw ex;
        } finally {
            if (null != session) {
                session.close();
            }
        }
    }

    //获取EventLog列表，分页
    private PageDivisionQueryResultModel<EventLogEntity> getPageDivisionEventLogEntityList(int pageIndex) throws Exception {
        pageDivisionQueryUtil<EventLogEntity> util = new pageDivisionQueryUtil();

        try {
            String queryHQL = "from EventLogEntity ee order by ee.createTime desc";
            String countHQL = "select count(*) from EventLogEntity ee";
            return util.excutePageDivisionQuery(pageIndex, queryHQL, countHQL);
        } catch (Exception ex) {
            LogUtil.SaveLog(eventLogOperation.class.getName(), ex);
            throw ex;
        }
    }

    //通过SubscriberId获取指定的Subscriber的日志
    private List<EventLogEntity> getEventLogListBySubscriberId(int SubscriberId, int Count) throws Exception {
        List<EventLogEntity> elList = null;

        try {
            session = hibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            Query q = session.createQuery("from EventLogEntity el where el.subscriber.id = :SubscriberId order by el.createTime desc");
            q.setParameter("SubscriberId", SubscriberId);
            if (Count > 0) {
                q.setFirstResult(0);
                q.setMaxResults(Count);
            }
            elList = (List<EventLogEntity>) q.list();
            return elList;
        } catch (Exception ex) {
            LogUtil.SaveLog(eventLogOperation.class.getName(), ex);
            throw ex;
        } finally {
            session.close();
        }
    }

    //通过EventsourceId获取指定的Eventsource的日志
    private List<EventLogEntity> getEventLogListByEventsourceId(int EventsourceId, int Count) throws Exception {
        List<EventLogEntity> elList = null;

        try {
            session = hibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            Query q = session.createQuery("from EventLogEntity el where el.eventsource.id = :EventsourceId order by el.createTime desc");
            q.setParameter("EventsourceId", EventsourceId);
            if (Count > 0) {
                q.setFirstResult(0);
                q.setMaxResults(Count);
            }
            elList = (List<EventLogEntity>) q.list();
            return elList;
        } catch (Exception ex) {
            LogUtil.SaveLog(eventLogOperation.class.getName(), ex);
            throw ex;
        } finally {
            session.close();
        }
    }

    private EventLogModel ConvertEventlogEntity2Model(EventLogEntity entity) throws Exception {
        esOp = new eventSourceOperation();
        subOp = new subscriberOperation();
        EventLogModel log = new EventLogModel();
        try {
            log.setCreateTime(entity.getCreateTime());
            log.setTitle(entity.getTitle());
            log.setId(entity.getId());
            log.setLevel(Enum.Level.values()[entity.getLevel()]);
            log.setMessage(entity.getMessage());
            log.setEventSourceModel(esOp.ConvertEventsourceEntity2Model(entity.getEventsource()));
            log.setSubscriberModel(subOp.ConvertSubscriberEntity2Model(entity.getSubscriber()));
            return log;
        } catch (Exception ex) {
            LogUtil.SaveLog(eventLogOperation.class.getName(), ex);
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
