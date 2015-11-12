package enno.operation.dal;

/**
 * Created by sabermai on 2015/11/13.
 */
public class EventsourceTemplateActivityEntity {
    private int id;
    private String name;
    private String displayName;
    private String comments;
    private Integer allowNull;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Integer getAllowNull() {
        return allowNull;
    }

    public void setAllowNull(Integer allowNull) {
        this.allowNull = allowNull;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EventsourceTemplateActivityEntity that = (EventsourceTemplateActivityEntity) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (displayName != null ? !displayName.equals(that.displayName) : that.displayName != null) return false;
        if (comments != null ? !comments.equals(that.comments) : that.comments != null) return false;
        if (allowNull != null ? !allowNull.equals(that.allowNull) : that.allowNull != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
        result = 31 * result + (comments != null ? comments.hashCode() : 0);
        result = 31 * result + (allowNull != null ? allowNull.hashCode() : 0);
        return result;
    }
}
