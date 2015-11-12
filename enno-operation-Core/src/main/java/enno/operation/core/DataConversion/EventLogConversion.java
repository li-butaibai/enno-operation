package enno.operation.core.DataConversion;

import enno.operation.core.model.EventLogModel;
import enno.operation.core.model.EventSourceModel;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;
import java.util.Map;

/**
 * Created by v-zoli on 2015/11/11.
 */
public class EventLogConversion implements IDataConversion<EventLogModel> {
    public EventLogModel Decode(String path, String data) {
        return null;
    }

    public String Encode(EventLogModel model) {
        throw new NotImplementedException();
    }

    public List<EventLogModel> Decode(Map<String, String> nodes) {
        throw new NotImplementedException();

    }
}
