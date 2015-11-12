package enno.operation.zkmodel;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * Created by v-zoli on 2015/11/12.
 */
public class EventSourceConnectModel {
    private String sourceId;
    private String protocol;
    private String eventDecoder;
    private Map<String,String> eventSourceActivities;

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getEventDecoder() {
        return eventDecoder;
    }

    public void setEventDecoder(String eventDecoder) {
        this.eventDecoder = eventDecoder;
    }

    public Map<String, String> getEventSourceActivities() {
        return eventSourceActivities;
    }

    public void setEventSourceActivities(Map<String, String> eventSourceActivities) {
        this.eventSourceActivities = eventSourceActivities;
    }
}
