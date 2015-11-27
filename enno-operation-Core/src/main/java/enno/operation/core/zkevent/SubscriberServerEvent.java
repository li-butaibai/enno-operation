package enno.operation.core.zkevent;

import enno.operation.ZKListener.SubscriberClusterListener;
import enno.operation.core.common.LogUtil;
import enno.operation.dal.SubscriberEntity;
import enno.operation.dal.hibernateUtil;
import enno.operation.zkmodel.SubscriberData;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * Created by v-zoli on 2015/11/10.
 */
public class SubscriberServerEvent implements SubscriberClusterListener {
    public void process(List<SubscriberData> subscriberDataList) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = hibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            List<SubscriberEntity> subscriberEntityList =
                    session.createQuery("from SubscriberEntity fetch all properties where dataStatus=1").list();
            for (SubscriberEntity subscriberEntity : subscriberEntityList) {
                boolean exist = false;
                for (SubscriberData subscriberData : subscriberDataList) {
                    if (subscriberData.getSubscriberId().equals(subscriberEntity.getName())) {
                        exist = true;
                        break;
                    }
                }
                subscriberEntity.setDataStatus(exist ? 1 : 0);
                session.save(subscriberEntity);
            }
            for (SubscriberData subscriberData : subscriberDataList) {
                String sHql = "from SubscriberEntity fetch all properties where name=:subscriberName and dataStatus=1";
                Query sQuery = session.createQuery(sHql);
                sQuery.setString("subscriberName", subscriberData.getSubscriberId());
                Object subscriberObject = sQuery.uniqueResult();
                if (subscriberObject == null) {
                    SubscriberEntity subscriberEntity = new SubscriberEntity();
                    subscriberEntity.setName(subscriberData.getSubscriberId());
                    subscriberEntity.setComments(subscriberData.getSubscriberData());
                    subscriberEntity.setDataStatus(1);
                    subscriberEntity.setStatus(1);
                    session.save(subscriberEntity);
                }
            }
            transaction.commit();
        } catch (Exception ex) {
            if(transaction!=null)
            {
                transaction.rollback();
            }
            //ex.printStackTrace();
            LogUtil.SaveLog(SubscriberServerEvent.class.getName(), ex);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
