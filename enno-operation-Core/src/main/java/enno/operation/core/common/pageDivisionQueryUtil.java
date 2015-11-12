package enno.operation.core.common;

import enno.operation.core.model.PageDivisionQueryResultModel;
import enno.operation.dal.hibernateHelper;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by sabermai on 2015/11/11.
 */
public class pageDivisionQueryUtil<T> {
    private static Session session = null;

    public pageDivisionQueryUtil() {
        session = hibernateHelper.getSessionFactory().openSession();
    }

    public PageDivisionQueryResultModel<T> excutePageDivisionQuery(int currentPageIndex, int pageSize, String queryHqlStatement, String countHqlStatement) throws Exception {
        PageDivisionQueryResultModel<T> resultModel = new PageDivisionQueryResultModel();
        List<T> result = null;

        try {
            Transaction tx = session.beginTransaction();
            Query q = session.createQuery(queryHqlStatement);
            q.setFirstResult((--currentPageIndex) * pageSize + 1);
            q.setMaxResults(pageSize);
            result = (List<T>) q.list();

            q = session.createQuery(countHqlStatement);
            int count = (Integer)q.uniqueResult();

            resultModel.setCurrentPageIndex(currentPageIndex);
            resultModel.setPageSize(pageSize);
            resultModel.setRecordCount(count);
            resultModel.setQueryResult(result);
            return resultModel;
        } catch (Exception ex) {
            throw ex;
        } finally {
            if (null != session) {
                session.close();
            }
        }
    }
}
