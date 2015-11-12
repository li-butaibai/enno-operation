package enno.operation.zkmodel;

import java.util.List;

/**
 * Created by v-zoli on 2015/11/12.
 */
public class EventSourceData {
    private String eventSourceId;
    private List<SubscriberData> subscribers;

    public String getEventSourceId() {
        return eventSourceId;
    }

    public void setEventSourceId(String eventSourceId) {
        this.eventSourceId = eventSourceId;
    }

    public List<SubscriberData> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(List<SubscriberData> subscribers) {
        this.subscribers = subscribers;
    }
}
