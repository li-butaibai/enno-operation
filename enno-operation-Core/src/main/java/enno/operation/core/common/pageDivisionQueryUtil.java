package enno.operation.core.common;

import enno.operation.core.model.PageDivisionQueryResultModel;
import enno.operation.dal.hibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * Created by sabermai on 2015/11/11.
 */
public class pageDivisionQueryUtil<T> {
    private static Session session = null;

    public pageDivisionQueryUtil() {
        session = hibernateUtil.getSessionFactory().openSession();
    }

    public PageDivisionQueryResultModel<T> excutePageDivisionQuery(int currentPageIndex, String queryHqlStatement, String countHqlStatement) throws Exception {
        PageDivisionQueryResultModel<T> resultModel = new PageDivisionQueryResultModel();
        List<T> result = null;

        try {
            Transaction tx = session.beginTransaction();
            Query q = session.createQuery(queryHqlStatement);
            q.setFirstResult((currentPageIndex - 1) * resultModel.getPageSize());
            q.setMaxResults(resultModel.getPageSize());
            result = (List<T>) q.list();

            q = session.createQuery(countHqlStatement);
            long count = (Long) q.uniqueResult();

            resultModel.setCurrentPageIndex(currentPageIndex);
            //resultModel.setPageSize(pageSize);
            resultModel.setRecordCount((int) count);
            resultModel.setQueryResult(result);
            resultModel.setPageCount();
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
