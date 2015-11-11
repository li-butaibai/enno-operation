package enno.operation.core.model;

import java.awt.*;

/**
 * Created by sabermai on 2015/11/11.
 */
public class EventLogModel {
    private int id;
    private int eventsourceId;
    private EventSourceModel eventSourceModel;
    private int level;
    private int subscriberId;
    private SubscriberModel subscriberModel;
    private String title;
    private String message;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getEventsourceId() {
        return eventsourceId;
    }

    public void setEventsourceId(int eventsourceId) {
        this.eventsourceId = eventsourceId;
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

    public int getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(int subscriberId) {
        this.subscriberId = subscriberId;
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
}
