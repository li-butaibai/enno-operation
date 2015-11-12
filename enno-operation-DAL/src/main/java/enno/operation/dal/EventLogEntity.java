package enno.operation.dal;

import java.sql.Timestamp;

/**
 * Created by sabermai on 2015/11/13.
 */
public class EventLogEntity {
    private int id;
    //private Integer eventSourceId;
    private Integer subscriberId;
    private String title;
    private Integer level;
    private String message;
    private Timestamp createTime;
    private EventsourceEntity eventsourceEntity;

    public EventsourceEntity getEventsourceEntity() {
        return eventsourceEntity;
    }

    public void setEventsourceEntity(EventsourceEntity eventsourceEntity) {
        this.eventsourceEntity = eventsourceEntity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /*public Integer getEventSourceId() {
        return eventSourceId;
    }

    public void setEventSourceId(Integer eventSourceId) {
        this.eventSourceId = eventSourceId;
    }*/

    public Integer getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(Integer subscriberId) {
        this.subscriberId = subscriberId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventLogEntity that = (EventLogEntity) o;

        if (id != that.id) return false;
//        if (eventSourceId != null ? !eventSourceId.equals(that.eventSourceId) : that.eventSourceId != null)
//            return false;
        if (subscriberId != null ? !subscriberId.equals(that.subscriberId) : that.subscriberId != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (level != null ? !level.equals(that.level) : that.level != null) return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        //result = 31 * result + (eventSourceId != null ? eventSourceId.hashCode() : 0);
        result = 31 * result + (subscriberId != null ? subscriberId.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (level != null ? level.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        return result;
    }
}
