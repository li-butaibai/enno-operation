package enno.operation.dal;

import javax.persistence.*;

/**
 * Created by EriclLee on 15/11/10.
 */
@Entity
@Table(name = "eventsource_activity", schema = "", catalog = "enno_operationserverdb")
public class EventsourceActivityEntity {
    private int id;
    private Integer eventSourceId;
    private Integer templateActivityId;
    private String value;

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
    @Column(name = "TemplateActivityId", nullable = true, insertable = true, updatable = true)
    public Integer getTemplateActivityId() {
        return templateActivityId;
    }

    public void setTemplateActivityId(Integer templateActivityId) {
        this.templateActivityId = templateActivityId;
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
        if (eventSourceId != null ? !eventSourceId.equals(that.eventSourceId) : that.eventSourceId != null)
            return false;
        if (templateActivityId != null ? !templateActivityId.equals(that.templateActivityId) : that.templateActivityId != null)
            return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (eventSourceId != null ? eventSourceId.hashCode() : 0);
        result = 31 * result + (templateActivityId != null ? templateActivityId.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
