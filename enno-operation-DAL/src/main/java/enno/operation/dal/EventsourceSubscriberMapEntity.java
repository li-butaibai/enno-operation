package enno.operation.dal;

import javax.persistence.*;

/**
 * Created by sabermai on 2015/11/13.
 */
@Entity
@Table(name = "eventsource_subscriber_map", schema = "", catalog = "enno_operationserverdb")
public class EventsourceSubscriberMapEntity {
    private int id;
    private EventsourceEntity eventsource;
    private SubscriberEntity subscriber;

    @Id
    @Column(name = "Id", nullable = false, insertable = true, updatable = true)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventsourceSubscriberMapEntity that = (EventsourceSubscriberMapEntity) o;

        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @ManyToOne
    @JoinColumn(name = "EventSourceId", referencedColumnName = "Id", nullable = false)
    public EventsourceEntity getEventsource() {
        return eventsource;
    }

    public void setEventsource(EventsourceEntity eventsource) {
        this.eventsource = eventsource;
    }

    @ManyToOne
    @JoinColumn(name = "SubscriberId", referencedColumnName = "Id", nullable = false)
    public SubscriberEntity getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(SubscriberEntity subscriber) {
        this.subscriber = subscriber;
    }
}
