package enno.operation.dal;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by sabermai on 2015/11/13.
 */
@Entity
@Table(name = "eventsource", schema = "", catalog = "enno_operationserverdb")
public class EventsourceEntity {
    private int id;
    private String sourceId;
    private String eventDecoder;
    private Timestamp createTime;
    private Timestamp updateTime;
    private String comments;
    private Integer status;
    private Integer dataStatus;
    private EventsourceTemplateEntity eventsourceTemplate;

    @Id
    @Column(name = "Id", nullable = false, insertable = true, updatable = true)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "SourceId", nullable = true, insertable = true, updatable = true, length = 45)
    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    @Basic
    @Column(name = "EventDecoder", nullable = true, insertable = true, updatable = true, length = 45)
    public String getEventDecoder() {
        return eventDecoder;
    }

    public void setEventDecoder(String eventDecoder) {
        this.eventDecoder = eventDecoder;
    }

    @Basic
    @Column(name = "CreateTime", nullable = true, insertable = true, updatable = true)
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "UpdateTime", nullable = true, insertable = true, updatable = true)
    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Basic
    @Column(name = "Comments", nullable = true, insertable = true, updatable = true, length = 200)
    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    @Basic
    @Column(name = "Status", nullable = true, insertable = true, updatable = true)
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Basic
    @Column(name = "DataStatus", nullable = true, insertable = true, updatable = true)
    public Integer getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(Integer dataStatus) {
        this.dataStatus = dataStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventsourceEntity that = (EventsourceEntity) o;

        if (id != that.id) return false;
        if (sourceId != null ? !sourceId.equals(that.sourceId) : that.sourceId != null) return false;
        if (eventDecoder != null ? !eventDecoder.equals(that.eventDecoder) : that.eventDecoder != null) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (updateTime != null ? !updateTime.equals(that.updateTime) : that.updateTime != null) return false;
        if (comments != null ? !comments.equals(that.comments) : that.comments != null) return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (dataStatus != null ? !dataStatus.equals(that.dataStatus) : that.dataStatus != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (sourceId != null ? sourceId.hashCode() : 0);
        result = 31 * result + (eventDecoder != null ? eventDecoder.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (dataStatus != null ? dataStatus.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "EventSourceTemplateId", referencedColumnName = "Id", nullable = false)
    public EventsourceTemplateEntity getEventsourceTemplate() {
        return eventsourceTemplate;
    }

    public void setEventsourceTemplate(EventsourceTemplateEntity eventsourceTemplate) {
        this.eventsourceTemplate = eventsourceTemplate;
    }
}
