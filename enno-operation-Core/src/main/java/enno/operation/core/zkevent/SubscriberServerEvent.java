package enno.operation.core.zkevent;

import enno.operation.ZKListener.SubscriberClusterListener;
import enno.operation.core.DataConversion.DataConversionFactory;
import enno.operation.core.DataConversion.IDataConversion;
import enno.operation.core.DataConversion.SubscriberConversion;
import enno.operation.core.model.SubscriberModel;
import enno.operation.dal.EventsourceSubscriberMapEntity;
import enno.operation.dal.SubscriberEntity;
import enno.operation.dal.hibernateHelper;
import enno.operation.zkmodel.SubscriberData;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Map;

/**
 * Created by v-zoli on 2015/11/10.
 */
public class SubscriberServerEvent implements SubscriberClusterListener {
    public void process(List<SubscriberData> subscriberDataList) {
//        Session session = null;
//        try {
//            session = hibernateHelper.getSessionFactory().openSession();
//            Transaction transaction = null;
//            if (nodes.isEmpty()) {
//                try {
//                    transaction = session.beginTransaction();
//                    List<SubscriberEntity> subscriberEntityList = session.createQuery("from SubscriberEntity fetch all properties").list();
//                    for (SubscriberEntity subscriberEntity : subscriberEntityList) {
//                        //将所有订阅者状态置为离线，DB
//                        subscriberEntity.setStatus(1);
//                        //更新所有EventSource状态置为空闲（DB，ZK）
//                        List<EventsourceSubscriberMapEntity> eventsourceSubscriberMapEntities
//                                = session.createQuery("from EventsourceSubscriberMapEntity fetch all properties where subscriberId = " + subscriberEntity.getId()).list();
//                        for (EventsourceSubscriberMapEntity eventsourceSubscriberMapEntity : eventsourceSubscriberMapEntities) {
//                            session.delete(eventsourceSubscriberMapEntity);
//                        }
//                    }
//                    transaction.commit();
//                }catch (Exception ex)
//                {
//                    if(transaction!=null){
//                        transaction.rollback();
//                    }
//                    ex.printStackTrace();
//
//                }
//            } else {
//
//                //for (Map.Entry<String, String> node : nodes.entrySet()) {
//                //TODO：与数据库数据比较找到所有刚离线的订阅者
//                List<SubscriberEntity> subscriberEntityList = session.createQuery("from SubscriberEntity fetch all properties where status = 1").list();
//                for (SubscriberEntity subscriberEntity : subscriberEntityList) {
//                    boolean exist=false;
//                    for (Map.Entry<String, String> node : nodes.entrySet())
//                    {
//                        DataConversionFactory<SubscriberModel> dataConversionFactory = new DataConversionFactory<SubscriberModel>();
//                        IDataConversion<SubscriberModel> dataConversion = dataConversionFactory.createDataConversion(SubscriberConversion.class);
//                        SubscriberModel subscriberModel = dataConversion.Decode(node.getKey(), node.getValue());
//                        if(subscriberModel.getId() == subscriberEntity.getId()){
//                            exist=true;
//                            subscriberEntity.setStatus(1);
//                            break;
//                        }
//                    }
//                    if(!exist){
//
//                    }
//                }
//
//                //TODO: 将这些订阅者状态置为离线，DB
//
//                //TODO：更新这些订阅者订阅的所有EventSource状态置为空闲（DB，ZK）
//                //}
//                //TODO：更新nodes对应订阅者的状态为上线，如果是新上线的则注册到数据库
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//        finally {
//            if(session!=null)
//            {
//                session.close();
//            }
//        }
    }
}
