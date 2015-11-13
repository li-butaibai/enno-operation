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

    //获取EventSourceEntity列表，分页
    private PageDivisionQueryResultModel<EventsourceEntity> getEventSourceEntityList(int pageIndex, int pageSize) throws Exception {
        pageDivisionQueryUtil<EventsourceEntity> util = new pageDivisionQueryUtil();

        try {
            String queryHQL = "from EventsourceEntity es where es.status = 1 and es.dataStatus = 1 order by es.id asc";
            String countHQL = "select count(*) from EventsourceEntity es where es.status = 1 and es.dataStatus = 1";
            return util.excutePageDivisionQuery(pageIndex, pageSize, queryHQL, countHQL);
        } catch (Exception ex) {
            throw ex;
        }
    }

    public PageDivisionQueryResultModel<EventSourceModel> getEventSourceList(int pageIndex, int pageSize) throws Exception {
        PageDivisionQueryResultModel<EventSourceModel> qr = new PageDivisionQueryResultModel();
        try {
            PageDivisionQueryResultModel<EventsourceEntity> entityQR = getEventSourceEntityList(pageIndex, pageSize);
            List<EventSourceModel> esModelList = new ArrayList<EventSourceModel>();
            if (entityQR.getQueryResult() != null) {
                for (EventsourceEntity esEntity : entityQR.getQueryResult()) {
                    EventSourceModel esModel = new EventSourceModel();
                    esModel.setComments(esEntity.getComments());
                    esModel.setCreateTime(esEntity.getCreateTime());
                    esModel.setEventDecoder(esEntity.getEventDecoder());
                    esModel.setEventSourceTemplateId(esEntity.getEventsourceTemplate().getId());
                    esModel.setId(esEntity.getId());
                    esModel.setSourceId(esEntity.getSourceId());
                    esModel.setUpdateTime(esEntity.getUpdateTime());
                    //esModel.setSubscriberList();
                    //esModel.setProtocol();
                    //esModel.setEventSourceActivities();
                }
            }
            return qr;
        } catch (Exception ex) {
            throw ex;
        }
    }

    //通过Id获取指定的EventSource
    public EventsourceEntity getEventSourceById(int Id) throws Exception {
        EventsourceEntity es = new EventsourceEntity();

        try {
            session = hibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            Query q = session.createQuery("from EventsourceEntity es where es.id = :EventsourceId");
            q.setParameter("EventsourceId", Id);
            es = (EventsourceEntity) q.uniqueResult();
            return es;
        } catch (Exception ex) {
            throw ex;
        } finally {
            session.close();
        }
    }

    //通过EventSource的id获取Activity
    private List<EventSourceActivityModel> getEventSourceActivityListByEventSourceId(int EventSourceId) throws Exception {
        List<EventSourceActivityModel> activityList = new ArrayList<EventSourceActivityModel>();

        try {
            session = hibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            /*Query q = session.createQuery("from EventsourceActivityEntity ae inner join ae.eventsourceTemplateActivity ta inner join ");
            q.setParameter("EventsourceId", Id);
            es = (EventsourceEntity) q.uniqueResult();*/
            return activityList;
        } catch (Exception ex) {
            throw ex;
        } finally {
            session.close();
        }
    }
}
