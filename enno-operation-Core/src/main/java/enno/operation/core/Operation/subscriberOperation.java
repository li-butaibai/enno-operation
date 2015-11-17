package enno.operation.core.Operation;

import enno.operation.core.common.pageDivisionQueryUtil;
import enno.operation.core.model.PageDivisionQueryResultModel;
import enno.operation.core.model.SubscriberModel;
import enno.operation.dal.SubscriberEntity;
import enno.operation.dal.hibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by sabermai on 2015/11/11.
 */
public class subscriberOperation {
    private Session session = null;
    private eventSourceOperation esOp = null;

    //获取订阅者列表，分页，PageIndex：当前页码，PageSize：每页记录条数
    public PageDivisionQueryResultModel<SubscriberModel> getPageDivisonSubscriberList(int pageIndex, int pageSize) throws Exception {
        PageDivisionQueryResultModel<SubscriberModel> result = new PageDivisionQueryResultModel<SubscriberModel>();

        try {
            PageDivisionQueryResultModel<SubscriberEntity> entityList = new PageDivisionQueryResultModel<SubscriberEntity>();
            entityList = getPageDivisonSubscriberEntityList(pageIndex, pageSize);
            result.setPageSize(pageSize);
            result.setRecordCount(entityList.getRecordCount());
            result.setCurrentPageIndex(pageIndex);
            result.setQueryResult(ConvertSubscriberEntityList2ModelList(entityList.getQueryResult()));
            return result;
        } catch (Exception ex) {
            throw ex;
        }
    }

    //获取订阅者详情 Done
    public SubscriberModel getSubscriberById(int SubscriberId) throws Exception {
        try {
            esOp = new eventSourceOperation();
            SubscriberEntity subEntity = getSubscriberEntityById(SubscriberId);
            SubscriberModel suber = ConvertSubscriberEntity2Model(subEntity);
            suber.setEventsourceList(esOp.GetEventsourcesBySubscriberId(SubscriberId));
            return suber;
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (null != session) {
                session.close();
            }
        }
    }

    //通过EventSource的id获取Subscriber列表
    public List<SubscriberModel> getSubscriberListByEventSourceId(int EventSourceId) throws Exception {
        List<SubscriberModel> suberList = new ArrayList<SubscriberModel>();

        try {
            List<SubscriberEntity> suberEntityList = getSubscriberEntityListByEventSourceId(EventSourceId);
            if (suberEntityList != null) {
                for (SubscriberEntity sub : suberEntityList) {
                    SubscriberModel suber = new SubscriberModel();
                    suber = ConvertSubscriberEntity2Model(sub);
                    //suber.setEventsourceList(esOp.GetEventsourcesBySubscriberId(sub.getId()));
                    suberList.add(suber);
                }
            }
            return suberList;
        } catch (Exception ex) {
            throw ex;
        }
    }

    public void DeleteSubscriber(int SubscriberId) throws Exception {
        try {
            session = hibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            Query q = session.createQuery("update SubscriberEntity e set e.dataStatus = 0, e.status = 0 where e.id = :SubscriberId").setParameter("SubscriberId", SubscriberId);
            q.executeUpdate();
            tx.commit();
        } catch (Exception ex) {

        } finally {
            if (null != session) {
                session.close();
            }
        }
    }

    public void UpdateSubscriber(SubscriberModel Subscriber) throws Exception {
        try {
            session = hibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            Query q = session.createQuery("from SubscriberEntity sub where sub.id = :SubscriberId").setParameter("SubscriberId", Subscriber.getId());
            SubscriberEntity sub = (SubscriberEntity) q.uniqueResult();
            sub.setName(Subscriber.getName());
            sub.setComments(Subscriber.getComments());
            sub.setAddress(Subscriber.getAddress());
            tx.commit();
        } catch (Exception ex) {

        } finally {
            if (null != session) {
                session.close();
            }
        }
    }

    private SubscriberEntity getSubscriberEntityById(int SubscriberId) throws Exception {
        try {
            session = hibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            Query q = session.createQuery("from SubscriberEntity sub where sub.id = :SubscriberId");
            q.setParameter("SubscriberId", SubscriberId);
            SubscriberEntity subEntity = (SubscriberEntity) q.uniqueResult();
            return subEntity;
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (null != session) {
                session.close();
            }
        }
    }

    private List<SubscriberEntity> getSubscriberEntityListByEventSourceId(int EventSourceId) throws Exception {
        List<SubscriberEntity> suberEntityList = new ArrayList<SubscriberEntity>();

        try {
            session = hibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            Query q = session.createQuery("select sub from EventsourceSubscriberMapEntity m join m.eventsource es join m.subscriber sub where sub.status = 1 and sub.dataStatus = 1 and es.id = :EventsourceId");
            q.setParameter("EventsourceId", EventSourceId);
            suberEntityList = (List<SubscriberEntity>) q.list();
            return suberEntityList;
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (null != session) {
                session.close();
            }
        }
    }

    //Done
    public SubscriberModel ConvertSubscriberEntity2Model(SubscriberEntity suberEntity) throws Exception {
        SubscriberModel suber = new SubscriberModel();

        suber.setId(suberEntity.getId());
        suber.setUpdateTime(suberEntity.getUpdateTime());
        suber.setAddress(suberEntity.getAddress());
        suber.setComments(suberEntity.getComments());
        suber.setCreateTime(suberEntity.getCreateTime());
        suber.setName(suberEntity.getName());

        return suber;
    }

    private List<SubscriberModel> ConvertSubscriberEntityList2ModelList(List<SubscriberEntity> entityList) throws Exception {
        esOp = new eventSourceOperation();
        List<SubscriberModel> result = new ArrayList<SubscriberModel>();
        for (SubscriberEntity entity : entityList) {
            SubscriberModel suber = new SubscriberModel();
            suber = ConvertSubscriberEntity2Model(entity);
            suber.setEventsourceList(esOp.GetEventsourcesBySubscriberId(entity.getId()));
            result.add(suber);
        }
        return result;
    }

    //获取Subscriber列表，分页
    private PageDivisionQueryResultModel<SubscriberEntity> getPageDivisonSubscriberEntityList(int pageIndex, int pageSize) throws Exception {
        pageDivisionQueryUtil<SubscriberEntity> util = new pageDivisionQueryUtil();

        try {
            String queryHQL = "from SubscriberEntity se where se.status = 1 and se.dataStatus = 1 order by se.id asc";
            String countHQL = "select count(*) from SubscriberEntity se where se.status = 1 and se.dataStatus = 1";
            return util.excutePageDivisionQuery(pageIndex, pageSize, queryHQL, countHQL);
        } catch (Exception ex) {
            throw ex;
        }
    }
}
