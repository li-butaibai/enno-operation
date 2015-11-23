package enno.operation.core.model;

/**
 * Created by sabermai on 2015/11/11.
 */
public class EventSourceActivityModel {
    private int id;
    private int templateActivityId;
    private String name;
    private String displayName;
    private String value;
    private String comments;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getTemplateActivityId() {
        return templateActivityId;
    }

    public void setTemplateActivityId(int templateActivityId) {
        this.templateActivityId = templateActivityId;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
