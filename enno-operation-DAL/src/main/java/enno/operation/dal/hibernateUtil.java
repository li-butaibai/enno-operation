package enno.operation.dal;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * Created by sabermai on 2015/11/10.
 */
public class hibernateUtil {
    private static final SessionFactory ourSessionFactory;
    private static Session session;
    //private static final ServiceRegistry serviceRegistry;

    static {
        try {
            ApplicationContext context =
                    new FileSystemXmlApplicationContext("/config/enno-operation/operation-server.xml");
            ourSessionFactory = (SessionFactory) context.getBean("sessionFactory");
//            Configuration configuration = new Configuration();
//            configuration.configure();
//
//            serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
//            ourSessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() throws HibernateException {
        return ourSessionFactory;
    }
}
