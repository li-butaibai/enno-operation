package enno.operation.core.Operation;

import enno.operation.core.common.pageDivisionQueryUtil;
import enno.operation.core.model.EventSourceActivityModel;
import enno.operation.core.model.EventSourceModel;
import enno.operation.core.model.PageDivisionQueryResultModel;
import enno.operation.dal.EventsourceEntity;
import enno.operation.dal.hibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.*;

/**
 * Created by sabermai on 2015/11/10.
 */
public class eventSourceOperation {
    private Session session = null;
    eventSourceActivityOpeartion activityOp = new eventSourceActivityOpeartion();
    subscriberOperation subOp = new subscriberOperation();

    //获取EventSource列表，分页，PageIndex：当前页码，PageSize：每页记录条数
    public PageDivisionQueryResultModel<EventSourceModel> getEventSourceList(int pageIndex, int pageSize) throws Exception {
        PageDivisionQueryResultModel<EventSourceModel> qr = new PageDivisionQueryResultModel();
        try {
            PageDivisionQueryResultModel<EventsourceEntity> entityQR = getEventSourceEntityList(pageIndex, pageSize);
            List<EventSourceModel> esModelList = new ArrayList<EventSourceModel>();
            esModelList = ConvertEventsourceEntityList2ModelList(entityQR.getQueryResult());
            qr.setCurrentPageIndex(pageIndex);
            qr.setRecordCount(entityQR.getRecordCount());
            qr.setPageSize(pageSize);
            qr.setQueryResult(esModelList);
            return qr;
        } catch (Exception ex) {
            throw ex;
        }
    }

    //EventSource详情
    public EventSourceModel GetEventsourceById(int EventsourceId) throws Exception {
        EventsourceEntity entity = getEventSourceById(EventsourceId);
        EventSourceModel eventsource = ConvertEventsourceEntity2Model(entity);
        eventsource.setSubscriberList(subOp.getSubscriberListByEventSourceId(EventsourceId));
        return eventsource;
    }

    public List<EventSourceModel> GetEventsourcesBySubscriberId(int SubscriberId) throws Exception {
        List<EventSourceModel> eventsources = new ArrayList<EventSourceModel>();
        try {
            List<EventsourceEntity> entities = getEventsourceEntityListBySubscriberId(SubscriberId);
            for (EventsourceEntity entity : entities) {
                EventSourceModel eventsource = new EventSourceModel();
                eventsource = ConvertEventsourceEntity2Model(entity);
                eventsources.add(eventsource);
            }
            return eventsources;
        } catch (Exception ex) {
            throw ex;
        }
    }

    //通过Id获取指定的EventSource
    private EventsourceEntity getEventSourceById(int EventsourceId) throws Exception {
        EventsourceEntity es = new EventsourceEntity();

        try {
            session = hibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            Query q = session.createQuery("from EventsourceEntity es where es.id = :EventsourceId order by es.sourceId");
            q.setParameter("EventsourceId", EventsourceId);
            es = (EventsourceEntity) q.uniqueResult();
            return es;
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (null != session) {
                session.close();
            }
        }
    }

    public EventSourceModel ConvertEventsourceEntity2Model(EventsourceEntity esEntity) throws Exception {
        EventSourceModel esModel = new EventSourceModel();
        List<EventSourceActivityModel> activities = activityOp.GetEventSourceActivityByEventsourceId(esEntity.getId(), esEntity.getEventsourceTemplate().getId());
        esModel.setCreateTime(esEntity.getCreateTime());
        esModel.setComments(esEntity.getComments());
        esModel.setUpdateTime(esEntity.getUpdateTime());
        esModel.setSourceId(esEntity.getSourceId());
        esModel.setEventDecoder(esEntity.getEventDecoder());
        esModel.setEventSourceActivities(activities);
        esModel.setEventSourceTemplateId(esEntity.getEventsourceTemplate().getId());
        esModel.setId(esEntity.getId());
        esModel.setProtocol(esEntity.getEventsourceTemplate().getProtocol());
        return esModel;
    }

    private List<EventSourceModel> ConvertEventsourceEntityList2ModelList(List<EventsourceEntity> entityList) throws Exception {
        List<EventSourceModel> result = new ArrayList<EventSourceModel>();
        for (EventsourceEntity entity : entityList) {
            EventSourceModel es = new EventSourceModel();
            es = ConvertEventsourceEntity2Model(entity);
            es.setSubscriberList(subOp.getSubscriberListByEventSourceId(entity.getId()));
            result.add(es);
        }
        return result;
    }

    private List<EventsourceEntity> getEventsourceEntityListBySubscriberId(int SubscriberId) throws Exception {
        List<EventsourceEntity> esEntityList = new ArrayList<EventsourceEntity>();

        try {
            session = hibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            Query q = session.createQuery("select es from EventsourceSubscriberMapEntity m join m.eventsource es join m.subscriber sub where es.status = 1 and es.dataStatus = 1 and sub.id = :SubscriberId order by es.sourceId");
            q.setParameter("SubscriberId", SubscriberId);
            esEntityList = (List<EventsourceEntity>) q.list();
            return esEntityList;
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (null != session) {
                session.close();
            }
        }
    }

    //获取EventSourceEntity列表，分页
    private PageDivisionQueryResultModel<EventsourceEntity> getEventSourceEntityList(int pageIndex, int pageSize) throws Exception {
        pageDivisionQueryUtil<EventsourceEntity> util = new pageDivisionQueryUtil();

        try {
            String queryHQL = "from EventsourceEntity es where es.status = 1 and es.dataStatus = 1 order by es.sourceId asc";
            String countHQL = "select count(*) from EventsourceEntity es where es.status = 1 and es.dataStatus = 1";
            return util.excutePageDivisionQuery(pageIndex, pageSize, queryHQL, countHQL);
        } catch (Exception ex) {
            throw ex;
        }
    }
}
