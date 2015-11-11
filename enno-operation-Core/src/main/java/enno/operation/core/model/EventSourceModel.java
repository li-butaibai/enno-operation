package enno.operation.core.model;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by sabermai on 2015/11/11.
 */
public class EventSourceModel {
    public int Id;
    public String sourceId;
    public int eventSourceTemplateId;
    public String protocol;
    public String eventDecoder;
    public List<EventSourceActivityModel> eventSourceActivities;
    public Timestamp createTime;
    public Timestamp updateTime;
    public String comments;
}
