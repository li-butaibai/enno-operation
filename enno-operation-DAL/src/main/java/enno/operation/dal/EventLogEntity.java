package enno.operation.dal;

import javax.persistence.*;

/**
 * Created by EriclLee on 15/11/10.
 */
@Entity
@Table(name = "event_log", schema = "", catalog = "enno_operationserverdb")
public class EventLogEntity {
    private int id;
    private Integer eventSourceId;
    private Integer subscriberId;
    private String title;
    private Integer level;
    private String message;

    @Id
    @Column(name = "Id", nullable = false, insertable = true, updatable = true)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "EventSourceId", nullable = true, insertable = true, updatable = true)
    public Integer getEventSourceId() {
        return eventSourceId;
    }

    public void setEventSourceId(Integer eventSourceId) {
        this.eventSourceId = eventSourceId;
    }

    @Basic
    @Column(name = "SubscriberId", nullable = true, insertable = true, updatable = true)
    public Integer getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(Integer subscriberId) {
        this.subscriberId = subscriberId;
    }

    @Basic
    @Column(name = "Title", nullable = true, insertable = true, updatable = true, length = 200)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name = "Level", nullable = true, insertable = true, updatable = true)
    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventLogEntity that = (EventLogEntity) o;

        if (id != that.id) return false;
        if (eventSourceId != null ? !eventSourceId.equals(that.eventSourceId) : that.eventSourceId != null)
            return false;
        if (subscriberId != null ? !subscriberId.equals(that.subscriberId) : that.subscriberId != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        if (level != null ? !level.equals(that.level) : that.level != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (eventSourceId != null ? eventSourceId.hashCode() : 0);
        result = 31 * result + (subscriberId != null ? subscriberId.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (level != null ? level.hashCode() : 0);
        return result;
    }

    @Basic
    @Column(name = "Message", nullable = true, insertable = true, updatable = true, length = 200)
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
