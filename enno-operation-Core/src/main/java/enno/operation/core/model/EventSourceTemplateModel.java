package enno.operation.core.model;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by EriclLee on 15/11/15.
 */
public class EventSourceTemplateModel {
    private int id;
    private String name;
    private String protocol;
    private Timestamp createTime;
    private Timestamp updateTime;
    private String comments;
    private List<EventSourceTemplateActivityModel> eventSourceTemplateActivityModels;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public List<EventSourceTemplateActivityModel> getEventSourceTemplateActivityModels() {
        return eventSourceTemplateActivityModels;
    }

    public void setEventSourceTemplateActivityModels(List<EventSourceTemplateActivityModel> eventSourceTemplateActivityModels) {
        this.eventSourceTemplateActivityModels = eventSourceTemplateActivityModels;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }
}
