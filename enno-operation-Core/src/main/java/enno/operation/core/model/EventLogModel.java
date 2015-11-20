package enno.operation.core.model;

import java.awt.*;
import java.sql.Timestamp;

/**
 * Created by sabermai on 2015/11/11.
 */
public class EventLogModel {
    private int id;
    private EventSourceModel eventSourceModel;
    private int level;
    private SubscriberModel subscriberModel;
    private String title;
    private String message;
    private Timestamp createTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public EventSourceModel getEventSourceModel() {
        return eventSourceModel;
    }

    public void setEventSourceModel(EventSourceModel eventSourceModel) {
        this.eventSourceModel = eventSourceModel;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public SubscriberModel getSubscriberModel() {
        return subscriberModel;
    }

    public void setSubscriberModel(SubscriberModel subscriberModel) {
        this.subscriberModel = subscriberModel;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}
