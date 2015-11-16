package enno.operation.core.model;

/**
 * Created by EriclLee on 15/11/15.
 */
public class EventSourceTemplateActivityModel {
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
}
