package enno.operation.core.Operation;

import enno.operation.core.common.pageDivisionQueryUtil;
import enno.operation.core.model.*;
import enno.operation.dal.EventsourceEntity;
import enno.operation.dal.EventsourceTemplateEntity;
import enno.operation.dal.hibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EriclLee on 15/11/15.
 */
public class eventSourceTemplateOperation {
    private Session session = null;
    private eventSourceTemplateActivityOperation etaOperation = new eventSourceTemplateActivityOperation();
    public List<EventSourceTemplateModel> getEventSourceTemplateList() throws Exception {

        try {
            List<EventSourceTemplateModel> estModelList = new ArrayList<EventSourceTemplateModel>();
            List<EventsourceTemplateEntity> eventsourceTemplateEntities = getEventSourceTemplateEntityList();
            for(EventsourceTemplateEntity eventsourceTemplateEntity : eventsourceTemplateEntities) {
                EventSourceTemplateModel eventSourceTemplateModel = ConvertESTempalteEntity2Model(eventsourceTemplateEntity);
                estModelList.add(eventSourceTemplateModel);
            }
            return estModelList;
        } catch (Exception ex) {
            throw ex;
        }
    }

    private List<EventsourceTemplateEntity> getEventSourceTemplateEntityList() throws Exception {
        List<EventsourceTemplateEntity> estEntityList = new ArrayList<EventsourceTemplateEntity>();

        try {
            session = hibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            Query q = session.createQuery("select est from EventsourceTemplateEntity est fetch all properties");
            estEntityList = (List<EventsourceTemplateEntity>) q.list();
            return estEntityList;
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (null != session) {
                session.close();
            }
        }
    }

    private EventSourceTemplateModel ConvertESTempalteEntity2Model(EventsourceTemplateEntity estEntity) throws Exception {
        EventSourceTemplateModel estModel = new EventSourceTemplateModel();
        List<EventSourceTemplateActivityModel> activities = etaOperation.getEventSourceTemplateActivityByTemplateId(estEntity.getId());
        estModel.setCreateTime(estEntity.getCreateTime());
        estModel.setComments(estEntity.getComments());
        estModel.setUpdateTime(estEntity.getUpdateTime());
        estModel.setId(estEntity.getId());
        estModel.setName(estEntity.getName());
        estModel.setProtocol(estEntity.getProtocol());
        estModel.setEventSourceTemplateActivityModels(activities);
        return estModel;
    }
}
