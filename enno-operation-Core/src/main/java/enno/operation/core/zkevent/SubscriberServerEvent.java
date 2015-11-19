package enno.operation.core.zkevent;

import enno.operation.ZKListener.SubscriberClusterListener;
import enno.operation.core.common.LogUtil;
import enno.operation.dal.SubscriberEntity;
import enno.operation.dal.hibernateUtil;
import enno.operation.zkmodel.SubscriberData;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.List;

/**
 * Created by v-zoli on 2015/11/10.
 */
public class SubscriberServerEvent implements SubscriberClusterListener {
    public void process(List<SubscriberData> subscriberDataList) {
        Session session = null;
        try {
            session = hibernateUtil.getSessionFactory().openSession();
            List<SubscriberEntity> subscriberEntityList =
                    session.createQuery("from SubscriberEntity fetch all properties where dataStatus=0").list();
            for (SubscriberEntity subscriberEntity : subscriberEntityList) {
                boolean exist = false;
                for (SubscriberData subscriberData : subscriberDataList) {
                    if (subscriberData.getSubscriberId().equals(subscriberEntity.getName())) {
                        exist = true;
                        break;
                    }
                }
                subscriberEntity.setDataStatus(exist ? 0 : 1);
            }
            for (SubscriberData subscriberData : subscriberDataList) {
                String sHql = "from SubscriberEntity fetch all properties where name=:subscriberName and dataStatus=0";
                Query sQuery = session.createQuery(sHql);
                sQuery.setString("subscriberName", subscriberData.getSubscriberId());
                Object subscriberObject = sQuery.uniqueResult();
                if (subscriberObject == null) {
                    SubscriberEntity subscriberEntity = new SubscriberEntity();
                    subscriberEntity.setName(subscriberData.getSubscriberId());
                    subscriberEntity.setComments(subscriberData.getSubscriberData());
                    subscriberEntity.setDataStatus(0);
                    subscriberEntity.setStatus(0);
                    session.save(subscriberEntity);
                }
            }
        } catch (Exception ex) {
            //ex.printStackTrace();
            LogUtil.SaveLog(SubscriberServerEvent.class.getName(), ex);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
