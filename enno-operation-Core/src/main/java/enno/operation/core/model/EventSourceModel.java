package enno.operation.core.model;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by sabermai on 2015/11/11.
 */
public class EventSourceModel {
    private int Id;
    private String sourceId;
    private int eventSourceTemplateId;
    private String protocol;
    private String eventDecoder;
    private List<EventSourceActivityModel> eventSourceActivities;
    private List<SubscriberModel> subscriberList;
    private Timestamp createTime;
    private Timestamp updateTime;
    private String comments;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public int getEventSourceTemplateId() {
        return eventSourceTemplateId;
    }

    public void setEventSourceTemplateId(int eventSourceTemplateId) {
        this.eventSourceTemplateId = eventSourceTemplateId;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getEventDecoder() {
        return eventDecoder;
    }

    public void setEventDecoder(String eventDecoder) {
        this.eventDecoder = eventDecoder;
    }

    public List<EventSourceActivityModel> getEventSourceActivities() {
        return eventSourceActivities;
    }

    public void setEventSourceActivities(List<EventSourceActivityModel> eventSourceActivities) {
        this.eventSourceActivities = eventSourceActivities;
    }

    public List<SubscriberModel> getSubscriberList() {
        return subscriberList;
    }

    public void setSubscriberList(List<SubscriberModel> subscriberList) {
        this.subscriberList = subscriberList;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
