package enno.operation.zkmodel;

/**
 * Created by EriclLee on 15/11/29.
 */
public class ZKSource {
    private String connectString = "";
    private int sessionTimeout = 1000;
    private String subscriberRootName = "/SubscriberClusterRoot";
    private String eventSourceRootName = "/EventSourceRoot";
    private String eventLogRootName = "/EventLogRoot";

    public String getConnectString() {
        return connectString;
    }

    public void setConnectString(String connectString) {
        this.connectString = connectString;
    }

    public int getSessionTimeout() {
        return sessionTimeout;
    }

    public void setSessionTimeout(int sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public String getSubscriberRootName() {
        return subscriberRootName;
    }

    public void setSubscriberRootName(String subscriberRootName) {
        this.subscriberRootName = subscriberRootName;
    }

    public String getEventSourceRootName() {
        return eventSourceRootName;
    }

    public void setEventSourceRootName(String eventSourceRootName) {
        this.eventSourceRootName = eventSourceRootName;
    }

    public String getEventLogRootName() {
        return eventLogRootName;
    }

    public void setEventLogRootName(String eventLogRootName) {
        this.eventLogRootName = eventLogRootName;
    }
}
