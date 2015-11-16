import enno.operation.dal.*;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.Query;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import javax.transaction.Transaction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by EriclLee on 15/11/10.
 */
public class Main {
    private static final SessionFactory ourSessionFactory;
    private static final ServiceRegistry serviceRegistry;

    static {
        try {
            Configuration configuration = new Configuration();
            //configuration.configure();
            configuration.configure();
            serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
            ourSessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSession() throws HibernateException {
        return ourSessionFactory.openSession();
    }

    public static void main(final String[] args) throws Exception {
        final Session session = getSession();
        try {
//            System.out.println("querying all the managed entities...");
//            final Map metadataMap = session.getSessionFactory().getAllClassMetadata();
//            for (Object key : metadataMap.keySet()) {
//                final ClassMetadata classMetadata = (ClassMetadata) metadataMap.get(key);
//                final String entityName = classMetadata.getEntityName();
//                final Query query = session.createQuery("from " + entityName);
//                System.out.println("executing: " + query.getQueryString());
//                for (Object o : query.list()) {
//                    System.out.println("  " + o);
//                }
            org.hibernate.Transaction tx = session.beginTransaction();
            /*EventsourceActivityEntity e = new EventsourceActivityEntity();
            EventsourceTemplateActivityEntity t = new EventsourceTemplateActivityEntity();
            EventsourceEntity es = new EventsourceEntity();
            t.setId(1);
            es.setId(1);
            e.setValue("100");
            e.setEventsourceTemplateActivity(t);
            e.setEventsource(es);
            session.save(e);*/
            Query q = session.createQuery("From EventsourceActivityEntity e where e.id = 4");
            EventsourceActivityEntity e = (EventsourceActivityEntity)q.uniqueResult();
            session.delete(e);
            tx.commit();
            }
        catch (Exception ex){

        } finally {
            session.close();
        }
    }
}
