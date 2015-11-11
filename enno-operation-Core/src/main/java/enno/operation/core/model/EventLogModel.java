package enno.operation.core.model;

/**
 * Created by sabermai on 2015/11/11.
 */
public class EventLogModel {
    private int id;
    private int eventsourceId;
    private int level;
    private int subscriberId;
    private String title;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
