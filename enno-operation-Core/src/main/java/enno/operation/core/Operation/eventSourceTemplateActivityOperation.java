package enno.operation.core.Operation;

import enno.operation.core.model.EventSourceTemplateActivityModel;
import enno.operation.core.model.EventSourceTemplateModel;
import enno.operation.dal.EventsourceTemplateActivityEntity;
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
public class eventSourceTemplateActivityOperation {
    private Session session = null;
    public List<EventSourceTemplateActivityModel> getEventSourceTemplateActivityByTemplateId(int templateId) throws Exception
    {
        try {
            List<EventSourceTemplateActivityModel> estaModelList = new ArrayList<EventSourceTemplateActivityModel>();
            List<EventsourceTemplateActivityEntity> eventsourceTemplateActivityEntities = getEventSourceTemplateActivityEntityList(templateId);
            for(EventsourceTemplateActivityEntity eventsourceTemplateActivityEntity : eventsourceTemplateActivityEntities) {
                EventSourceTemplateActivityModel eventSourceTemplateActivityModelModel = ConvertESTAEntity2Model(eventsourceTemplateActivityEntity);
                estaModelList.add(eventSourceTemplateActivityModelModel);
            }
            return estaModelList;
        } catch (Exception ex) {
            throw ex;
        }
    }

    private List<EventsourceTemplateActivityEntity> getEventSourceTemplateActivityEntityList(int templateId) throws Exception {
        List<EventsourceTemplateActivityEntity> estaEntityList = new ArrayList<EventsourceTemplateActivityEntity>();
        try {
            session = hibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            String estaHql = "select esta from EventsourceTemplateActivityEntity esta join esta.eventsourceTemplate est where est.id =:templateId";
            Query q = session.createQuery(estaHql);
            q.setInteger("templateId", templateId);
            estaEntityList = (List<EventsourceTemplateActivityEntity>) q.list();
            return estaEntityList;
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (null != session) {
                session.close();
            }
        }
    }

    private EventSourceTemplateActivityModel ConvertESTAEntity2Model(EventsourceTemplateActivityEntity estaEntity) throws Exception {
        EventSourceTemplateActivityModel estaModel = new EventSourceTemplateActivityModel();
        estaModel.setAllowNull(estaEntity.getAllowNull());
        estaModel.setComments(estaEntity.getComments());
        estaModel.setId(estaEntity.getId());
        estaModel.setName(estaEntity.getName());
        estaModel.setDisplayName(estaEntity.getDisplayName());
        return estaModel;
    }

}
