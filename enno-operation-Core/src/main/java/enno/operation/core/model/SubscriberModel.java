package enno.operation.core.model;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by sabermai on 2015/11/11.
 */
public class SubscriberModel {
    private String name;
    private String address;
    private String comments;
    private List<EventSourceModel> eventsourceList;
    private Timestamp createTime;
    private Timestamp updateTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public List<EventSourceModel> getEventsourceList() {
        return eventsourceList;
    }

    public void setEventsourceList(List<EventSourceModel> eventsourceList) {
        this.eventsourceList = eventsourceList;
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
}
