package enno.operation.zkmodel;

/**
 * Created by v-zoli on 2015/11/12.
 */
public enum  EventType {
    Error("Error"),
    Alert("Alert"),
    Info("Info");
    private String eventType;
    public String getEventType() {
        return eventType;
    }
    private EventType(String eventType){
        this.eventType = eventType;
    }
}
