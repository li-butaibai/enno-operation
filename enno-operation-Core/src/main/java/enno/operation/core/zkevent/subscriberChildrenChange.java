package enno.operation.core.zkevent;

import enno.operation.dal.EventsourceSubscriberMapEntity;
import enno.operation.dal.SubscriberEntity;
import enno.operation.dal.hibernateHelper;
import enno.operation.zkl.ZKListener;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Map;

/**
 * Created by v-zoli on 2015/11/10.
 */
public class subscriberChildrenChange implements ZKListener {
    public void process(Map<String, String> nodes) {
        Session session = null;
        try {
            session = hibernateHelper.getSessionFactory().openSession();
            Transaction transaction = null;
            if (nodes.isEmpty()) {
                try {
                    transaction = session.beginTransaction();
                    List<SubscriberEntity> subscriberEntityList = session.createQuery("from SubscriberEntity fetch all properties").list();
                    for (SubscriberEntity subscriberEntity : subscriberEntityList) {
                        //将所有订阅者状态置为离线，DB
                        subscriberEntity.setStatus(1);
                        //更新所有EventSource状态置为空闲（DB，ZK）
                        List<EventsourceSubscriberMapEntity> eventsourceSubscriberMapEntities
                                = session.createQuery("from EventsourceSubscriberMapEntity fetch all properties where subscriberId = " + subscriberEntity.getId()).list();
                        for (EventsourceSubscriberMapEntity eventsourceSubscriberMapEntity : eventsourceSubscriberMapEntities) {
                            session.delete(eventsourceSubscriberMapEntity);
                        }
                    }
                    transaction.commit();
                }catch (Exception ex)
                {
                    if(transaction!=null){
                        transaction.rollback();
                    }
                    ex.printStackTrace();

                }
            } else {
//
//                for()
//                transaction = session.beginTransaction();
//                //TODO：与数据库数据比较找到所有刚离线的订阅者
//                List<SubscriberEntity> subscriberEntityList = session.createQuery("from SubscriberEntity fetch all properties where status = 1" ).list();
//                for(SubscriberEntity subscriberEntity : subscriberEntityList)
//                {
//                   for()
//                }
                //TODO: 将这些订阅者状态置为离线，DB
                //TODO：更新这些订阅者订阅的所有EventSource状态置为空闲（DB，ZK）
                //TODO：更新nodes对应订阅者的状态为上线，如果是新上线的则注册到数据库
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        finally {
            if(session!=null)
            {
                session.close();
            }
        }
    }
}
