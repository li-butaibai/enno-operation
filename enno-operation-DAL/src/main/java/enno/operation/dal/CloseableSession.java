package enno.operation.dal;

import org.hibernate.Session;

/**
 * Created by sabermai on 2015/12/2.
 */
public class CloseableSession implements AutoCloseable {
    private final Session session;

    public CloseableSession(Session session) {
        this.session = session;
    }

    public Session getSession(){
        return session;
    }

    public void close() throws Exception {
        session.close();
    }
}
