package enno.operation.dal;

import javax.persistence.*;

/**
 * Created by sabermai on 2015/11/13.
 */
@Entity
@Table(name = "eventsource_activity", schema = "", catalog = "enno_operationserverdb")
public class EventsourceActivityEntity {
    private int id;
    private String value;
    private EventsourceTemplateActivityEntity eventsourceTemplateActivity;
    private EventsourceEntity eventsource;

    @Id
    @Column(name = "Id", nullable = false, insertable = true, updatable = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "Value", nullable = true, insertable = true, updatable = true, length = 200)
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventsourceActivityEntity that = (EventsourceActivityEntity) o;

        if (id != that.id) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "TemplateActivityId", referencedColumnName = "Id")
    public EventsourceTemplateActivityEntity getEventsourceTemplateActivity() {
        return eventsourceTemplateActivity;
    }

    public void setEventsourceTemplateActivity(EventsourceTemplateActivityEntity eventsourceTemplateActivity) {
        this.eventsourceTemplateActivity = eventsourceTemplateActivity;
    }

    @ManyToOne
    @JoinColumn(name = "EventSourceId", referencedColumnName = "Id")
    public EventsourceEntity getEventsource() {
        return eventsource;
    }

    public void setEventsource(EventsourceEntity eventsource) {
        this.eventsource = eventsource;
    }
}
