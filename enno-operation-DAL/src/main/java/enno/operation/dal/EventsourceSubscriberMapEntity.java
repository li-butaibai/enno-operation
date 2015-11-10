package enno.operation.dal;

import javax.persistence.*;

/**
 * Created by EriclLee on 15/11/10.
 */
@Entity
@Table(name = "eventsource_subscriber_map", schema = "", catalog = "enno_operationserverdb")
public class EventsourceSubscriberMapEntity {
    private int id;
    private int eventSourceId;
    private int subscriberId;

    @Id
    @Column(name = "Id", nullable = false, insertable = true, updatable = true)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "EventSourceId", nullable = false, insertable = true, updatable = true)
    public int getEventSourceId() {
        return eventSourceId;
    }

    public void setEventSourceId(int eventSourceId) {
        this.eventSourceId = eventSourceId;
    }

    @Basic
    @Column(name = "SubscriberId", nullable = false, insertable = true, updatable = true)
    public int getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(int subscriberId) {
        this.subscriberId = subscriberId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventsourceSubscriberMapEntity that = (EventsourceSubscriberMapEntity) o;

        if (id != that.id) return false;
        if (eventSourceId != that.eventSourceId) return false;
        if (subscriberId != that.subscriberId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + eventSourceId;
        result = 31 * result + subscriberId;
        return result;
    }
}
