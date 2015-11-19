package enno.operation.core.model;

import java.util.List;

/**
 * Created by sabermai on 2015/11/11.
 * 分页查询结果的model
 */
public class PageDivisionQueryResultModel<T> {
    public int getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(int recordCount) {
        this.recordCount = recordCount;
    }

    public int getCurrentPageIndex() {
        return currentPageIndex;
    }

    public void setCurrentPageIndex(int currentPageIndex) {
        this.currentPageIndex = currentPageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    /*public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }*/

    public List<T> getQueryResult() {
        return queryResult;
    }

    public void setQueryResult(List<T> queryResult) {
        this.queryResult = queryResult;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount() {
        this.pageCount = pageSize == 0 ? 0: (int)Math.ceil((double)recordCount/pageSize);
    }

    private int recordCount;
    private int currentPageIndex;
    private int pageSize = 10;
    private List<T> queryResult;
    private int pageCount;
}
