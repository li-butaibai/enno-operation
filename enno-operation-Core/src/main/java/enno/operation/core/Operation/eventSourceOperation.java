package enno.operation.core.Operation;

import enno.operation.core.common.LogUtil;
import enno.operation.core.common.pageDivisionQueryUtil;
import enno.operation.core.model.*;
import enno.operation.core.model.Enum;
import enno.operation.dal.*;
import enno.operation.zkl.ZKClient;
import enno.operation.zkmodel.EventSourceConnectModel;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.Timestamp;
import java.util.*;

/**
 * Created by sabermai on 2015/11/10.
 */
public class eventSourceOperation {
    private Session session = null;
    eventSourceActivityOpeartion activityOp = null;
    subscriberOperation subOp = null;

    //region 对外提供的Public方法

    //获取EventSource列表，分页，PageIndex：当前页码，PageSize：每页记录条数
    public PageDivisionQueryResultModel<EventSourceModel> getEventSourceList(int pageIndex) throws Exception {
        PageDivisionQueryResultModel<EventSourceModel> qr = new PageDivisionQueryResultModel();
        try {
            PageDivisionQueryResultModel<EventsourceEntity> entityQR = getEventSourceEntityList(pageIndex);
            List<EventSourceModel> esModelList = new ArrayList<EventSourceModel>();
            esModelList = ConvertEventsourceEntityList2ModelList(entityQR.getQueryResult());
            qr.setCurrentPageIndex(pageIndex);
            qr.setRecordCount(entityQR.getRecordCount());
            //qr.setPageSize();
            qr.setQueryResult(esModelList);
            qr.setPageCount();
            return qr;
        } catch (Exception ex) {
            LogUtil.SaveLog(eventSourceOperation.class.getName(), ex);
            throw ex;
        }
    }

    //EventSource详情
    public EventSourceModel GetEventsourceById(int EventsourceId) throws Exception {
        subOp = new subscriberOperation();
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
            LogUtil.SaveLog(eventSourceOperation.class.getName(), ex);
            throw ex;
        }
    }

    public void AddEventsource(EventSourceModel eventSource) throws Exception {
        try {
            ZKClient zkCilent = ZKClient.getIntance();
            zkCilent.addEventSource(eventSource.getSourceId(), "");

            session = hibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();

            //Query Event Source Template
            int templateId = eventSource.getEventSourceTemplateId();
            Query q = session.createQuery("from EventsourceTemplateEntity t where t.id = :templateId");
            q.setParameter("templateId", templateId);
            EventsourceTemplateEntity templateEntity = (EventsourceTemplateEntity) q.uniqueResult();

            //Query Event Source Activity Template
            q = session.createQuery("from EventsourceTemplateActivityEntity ta where ta.eventsourceTemplate.id = :templateId");
            q.setParameter("templateId", templateId);
            List<EventsourceTemplateActivityEntity> templateActivities = q.list();

            EventsourceEntity entity = new EventsourceEntity();
            entity.setComments(eventSource.getComments());
            entity.setCreateTime(new Timestamp((new Date()).getTime()));
            entity.setEventDecoder(eventSource.getEventDecoder());
            entity.setEventsourceTemplate(templateEntity);
            entity.setUpdateTime(new Timestamp((new Date()).getTime()));
            entity.setSourceId(eventSource.getSourceId());
            entity.setDataStatus(Enum.validity.valid.ordinal());
            entity.setStatus(Enum.State.Online.ordinal());

            //List<EventsourceActivityEntity> activityEntities = new ArrayList<EventsourceActivityEntity>();
            for (EventSourceActivityModel activity : eventSource.getEventSourceActivities()) {
                EventsourceActivityEntity activityEntity = new EventsourceActivityEntity();
                activityEntity.setEventsource(entity);
                for (EventsourceTemplateActivityEntity tempActivity : templateActivities) {
                    if (tempActivity.getId() == activity.getTemplateActivityId()) {
                        activityEntity.setEventsourceTemplateActivity(tempActivity);
                    }
                    activityEntity.setValue(activity.getValue());
                }
                session.save(activityEntity);
            }


            session.save(entity);
            tx.commit();
        } catch (Exception ex) {
            LogUtil.SaveLog(eventSourceOperation.class.getName(), ex);
            throw ex;
        } finally {
            if (null != session) {
                session.close();
            }
        }
    }

    public void DeleteEventsource(int EventsourceId) throws Exception {
        try {
            EventsourceEntity es = getEventSourceById(EventsourceId);
            if (es.getStatus() == Enum.State.Offline.ordinal()) {
                ZKClient zkClient = ZKClient.getIntance();
                zkClient.removeEventSource(getEventSourceById(EventsourceId).getSourceId());

                session = hibernateUtil.getSessionFactory().openSession();
                Transaction tx = session.beginTransaction();

                //Delete Map Data
                Query q = session.createQuery("from EventsourceSubscriberMapEntity m where m.eventsource.id = :EventsourceId");
                q.setParameter("EventsourceId", EventsourceId);
                List<EventsourceSubscriberMapEntity> MapEntities = q.list();
                for (EventsourceSubscriberMapEntity MapEntity : MapEntities) {
                    session.delete(MapEntity);
                }

                q = session.createQuery("update EventsourceEntity e set e.dataStatus = :dataStatus, e.status = :status where e.id = :EventsourceId");
                q.setParameter("EventsourceId", EventsourceId);
                q.setParameter("dataStatus", Enum.validity.invalid.ordinal());
                q.setParameter("status", Enum.State.Offline.ordinal());
                q.executeUpdate();
                tx.commit();
            }
        } catch (Exception ex) {
            LogUtil.SaveLog(eventSourceOperation.class.getName(), ex);
            throw ex;
        } finally {
            if (null != session) {
                session.close();
            }
        }
    }

    public void AssignEventsource(int eventsourceId, int subscriberId) throws Exception {
        try {
            subOp = new subscriberOperation();
            ZKClient zkClient = ZKClient.getIntance();
            zkClient.setSubscriberData(String.valueOf(subscriberId), GetEventSourceConnectModels(subscriberId));

            session = hibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();

            /*Query q = session.createQuery("from EventsourceSubscriberMapEntity m where m.eventsource.id = :EventsourceId and m.subscriber.id = :SubscriberId");
            q.setParameter("EventsourceId", eventsourceId);
            q.setParameter("SubscriberId", subscriberId);
            List<EventsourceSubscriberMapEntity> MapEntities = q.list();
            if (MapEntities == null || MapEntities.size() <= 0) {
                EventsourceSubscriberMapEntity map = new EventsourceSubscriberMapEntity();
                EventsourceEntity es = new EventsourceEntity();
                es.setId(eventsourceId);
                SubscriberEntity sub = new SubscriberEntity();
                sub.setId(subscriberId);
                map.setEventsource(es);
                map.setSubscriber(sub);
                session.save(map);
                tx.commit();
            }*/
            EventsourceEntity es = new EventsourceEntity();
            es = getEventSourceById(eventsourceId);
            SubscriberEntity suber = new SubscriberEntity();
            suber = subOp.getSubscriberEntityById(subscriberId);

            EventLogEntity logEntity = new EventLogEntity();
            logEntity.setCreateTime(new Timestamp((new Date()).getTime()));
            logEntity.setEventsource(es);
            logEntity.setLevel(Enum.Level.Info.ordinal());
            logEntity.setSubscriber(suber);
            logEntity.setMessage(es.getSourceId() + "add subscription to " + suber.getName());
            logEntity.setTitle("Add Subscription");
            session.save(logEntity);
            tx.commit();
        } catch (Exception ex) {
            LogUtil.SaveLog(eventSourceOperation.class.getName(), ex);
            throw ex;
        } finally {
            if (null != session) {
                session.close();
            }
        }
    }

    public void RemoveEventsourceSubscription(int eventsourceId, int subscriberId) throws Exception {
        try {
            ZKClient zkClient = ZKClient.getIntance();
            zkClient.setSubscriberData(String.valueOf(subscriberId), GetEventSourceConnectModels(subscriberId));

            session = hibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();

            /*Query q = session.createQuery("from EventsourceSubscriberMapEntity m where m.eventsource.id = :EventsourceId and m.subscriber.id = :SubscriberId");
            q.setParameter("EventsourceId", eventsourceId);
            q.setParameter("SubscriberId", subscriberId);
            List<EventsourceSubscriberMapEntity> MapEntities = q.list();
            for (EventsourceSubscriberMapEntity MapEntity : MapEntities) {
                session.delete(MapEntity);
            }*/
            EventsourceEntity es = new EventsourceEntity();
            es = getEventSourceById(eventsourceId);
            SubscriberEntity suber = new SubscriberEntity();
            suber = subOp.getSubscriberEntityById(subscriberId);

            EventLogEntity logEntity = new EventLogEntity();
            logEntity.setCreateTime(new Timestamp((new Date()).getTime()));
            logEntity.setEventsource(es);
            logEntity.setLevel(Enum.Level.Info.ordinal());
            logEntity.setSubscriber(suber);
            logEntity.setMessage(es.getSourceId() + "cancels subscription to " + suber.getName());
            logEntity.setTitle("Cancel Subscription");
            session.save(logEntity);
            tx.commit();
        } catch (Exception ex) {
            LogUtil.SaveLog(eventSourceOperation.class.getName(), ex);
            throw ex;
        } finally {
            if (null != session) {
                session.close();
            }
        }
    }

    public void UpdateEventsource(EventSourceModel eventSource) throws Exception {
        try {
            session = hibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            //Query q = session.createQuery("from EventsourceEntity e where e.id = :EventsourceId").setParameter("EventsourceId", eventSource.getId());
            EventsourceEntity es = getEventSourceById(eventSource.getId());
            es.setSourceId(eventSource.getSourceId());
            es.setComments(eventSource.getComments());
            es.setEventDecoder(eventSource.getEventDecoder());
            es.setUpdateTime(new Timestamp((new Date()).getTime()));

            Query q = session.createQuery("from EventsourceActivityEntity ae where ae.eventsource.id = :EventsourceId").setParameter("EventsourceId", eventSource.getId());
            List<EventsourceActivityEntity> activityEntities = q.list();
            for (EventSourceActivityModel activityModel : eventSource.getEventSourceActivities()) {
                for (EventsourceActivityEntity activityEntity : activityEntities) {
                    if (activityEntity.getId() == activityModel.getId()) {
                        activityEntity.setValue(activityModel.getValue());
                    }
                }
            }
            tx.commit();
        } catch (Exception ex) {
            LogUtil.SaveLog(eventSourceOperation.class.getName(), ex);
            throw ex;
        } finally {
            if (null != session) {
                session.close();
            }
        }
    }

    public void OfflineEventsource(int eventsourceId) throws Exception {
        try {
            session = hibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            Query q = session.createQuery("from EventsourceSubscriberMapEntity m where m.eventsource.id = :EventsourceId");
            q.setParameter("EventsourceId", eventsourceId);
            List<EventsourceSubscriberMapEntity> MapEntities = q.list();
            if (MapEntities == null || MapEntities.size() == 0) {
                EventsourceEntity es = getEventSourceById(eventsourceId);
                es.setStatus(Enum.State.Offline.ordinal());
                tx.commit();
            }
        } catch (Exception ex) {
            LogUtil.SaveLog(eventSourceOperation.class.getName(), ex);
            throw ex;
        } finally {
            if (null != session) {
                session.close();
            }
        }
    }

    public boolean IsEventsourceNameExist(String Name) throws Exception {
        try {
            session = hibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            Query q = session.createQuery("from EventsourceEntity es where es.dataStatus = :dataStatus and es.sourceId = :name");
            q.setParameter("dataStatus", Enum.validity.valid);
            q.setParameter("name", Name);
            List<EventsourceEntity> Entities = q.list();
            if (Entities == null || Entities.size() == 0) {
                return false;
            }
            else{
                return true;
            }
        } catch (Exception ex) {
            LogUtil.SaveLog(eventSourceOperation.class.getName(), ex);
            throw ex;
        } finally {
            if (null != session) {
                session.close();
            }
        }
    }

    //获取未被指定Subcriber订阅的Eventsource列表
    public List<EventSourceModel> getUnSubscribedEventsourceListBySubscriberId(int SubscriberId) throws Exception {
        List<EventSourceModel> esList = new ArrayList<EventSourceModel>();

        try {
            List<EventsourceEntity> AllEventSourceEntites = getAllEventSourceEntities();
            List<EventsourceEntity> esEntityList = getEventsourceEntityListBySubscriberId(SubscriberId);
            AllEventSourceEntites.removeAll(esEntityList);

            for (EventsourceEntity es : AllEventSourceEntites) {
                EventSourceModel esModel = new EventSourceModel();
                esModel = ConvertEventsourceEntity2Model(es);
                esList.add(esModel);
            }

            return esList;
        } catch (Exception ex) {
            LogUtil.SaveLog(eventSourceOperation.class.getName(), ex);
            throw ex;
        }
    }
    //endregion

    //region private方法
    //通过Id获取指定的EventSource
    private EventsourceEntity getEventSourceById(int EventsourceId) throws Exception {
        EventsourceEntity es = new EventsourceEntity();

        try {
            session = hibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            session.flush();
            Query q = session.createQuery("from EventsourceEntity es where es.id = :EventsourceId order by es.sourceId");
            q.setParameter("EventsourceId", EventsourceId);
            es = (EventsourceEntity) q.uniqueResult();
            return es;
        } catch (Exception ex) {
            LogUtil.SaveLog(eventSourceOperation.class.getName(), ex);
            throw ex;
        } finally {
            if (null != session) {
                session.close();
            }
        }
    }

    public EventSourceModel ConvertEventsourceEntity2Model(EventsourceEntity esEntity) throws Exception {
        activityOp = new eventSourceActivityOpeartion();
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
        esModel.setEventSourceTemplateName(esEntity.getEventsourceTemplate().getName());
        esModel.setState(Enum.State.values()[esEntity.getStatus()]);
        return esModel;
    }

    private List<EventSourceModel> ConvertEventsourceEntityList2ModelList(List<EventsourceEntity> entityList) throws Exception {
        subOp = new subscriberOperation();
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
            Query q = session.createQuery("select es from EventsourceSubscriberMapEntity m join m.eventsource es join m.subscriber sub where es.dataStatus = 1 and sub.id = :SubscriberId order by es.sourceId");
            q.setParameter("SubscriberId", SubscriberId);
            esEntityList = (List<EventsourceEntity>) q.list();
            return esEntityList;
        } catch (Exception ex) {
            LogUtil.SaveLog(eventSourceOperation.class.getName(), ex);
            throw ex;
        } finally {
            if (null != session) {
                session.close();
            }
        }
    }

    //获取EventSourceEntity列表，分页
    private PageDivisionQueryResultModel<EventsourceEntity> getEventSourceEntityList(int pageIndex) throws Exception {
        pageDivisionQueryUtil<EventsourceEntity> util = new pageDivisionQueryUtil();

        try {
            String queryHQL = "from EventsourceEntity es where es.dataStatus = 1 order by es.sourceId asc";
            String countHQL = "select count(*) from EventsourceEntity es where es.status = 1 and es.dataStatus = 1";
            return util.excutePageDivisionQuery(pageIndex, queryHQL, countHQL);
        } catch (Exception ex) {
            LogUtil.SaveLog(eventSourceOperation.class.getName(), ex);
            throw ex;
        }
    }

    private List<EventSourceConnectModel> GetEventSourceConnectModels(int subscriberId) throws Exception {
        try {
            List<EventSourceConnectModel> ConnList = new ArrayList<EventSourceConnectModel>();

            session = hibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            Query q = session.createQuery("from EventsourceSubscriberMapEntity m where m.subscriber.id = :SubscriberId");
            q.setParameter("SubscriberId", subscriberId);
            List<EventsourceSubscriberMapEntity> SuberMapEntities = q.list();

            for (EventsourceSubscriberMapEntity mapEntity : SuberMapEntities) {
                EventSourceModel eventSource = ConvertEventsourceEntity2Model(mapEntity.getEventsource());
                EventSourceConnectModel connInfo = new EventSourceConnectModel();
                connInfo.setSourceId(eventSource.getSourceId());
                connInfo.setProtocol(eventSource.getProtocol());
                connInfo.setEventDecoder(eventSource.getEventDecoder());
                //获取原有的Activity信息
                Map<String, String> map = new HashMap<String, String>();
                for (EventSourceActivityModel activity : eventSource.getEventSourceActivities()) {
                    map.put(activity.getName(), activity.getValue());
                }
                connInfo.setEventSourceActivities(map);
                ConnList.add(connInfo);
            }

            return ConnList;
        } catch (Exception ex) {
            LogUtil.SaveLog(eventSourceOperation.class.getName(), ex);
            throw ex;
        }
    }

    private List<EventsourceEntity> getAllEventSourceEntities() throws Exception {
        List<EventsourceEntity> esEntityList = new ArrayList<EventsourceEntity>();

        try {
            session = hibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            Query q = session.createQuery("from EventsourceEntity es where es.dataStatus = :dataStatus");
            q.setParameter("dataStatus", Enum.validity.valid.ordinal());
            esEntityList = (List<EventsourceEntity>) q.list();
            return esEntityList;
        } catch (Exception ex) {
            LogUtil.SaveLog(eventSourceOperation.class.getName(), ex);
            throw ex;
        } finally {
            if (null != session) {
                session.close();
            }
        }
    }
    //endregion

}
