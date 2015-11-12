package enno.operation.ZKListener;

import enno.operation.zkmodel.EventLogData;

import java.util.EventListener;
import java.util.List;
import java.util.Map;

/**
 * Created by v-zoli on 2015/11/12.
 */
public interface EventLogListener extends EventListener {
    public void process(List<EventLogData> eventLogDataList);
}
