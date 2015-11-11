package enno.operation.core.zkevent;

import enno.operation.core.DataConversion.DataConversionFactory;
import enno.operation.core.DataConversion.IDataConversion;
import enno.operation.core.DataConversion.LogDataConversion;
import enno.operation.core.model.EventLogModel;
import enno.operation.zkl.ZKListener;

import java.awt.*;
import java.util.Map;

/**
 * Created by v-zoli on 2015/11/10.
 */
public class eventSourceChildrenChangeEvent implements ZKListener {
    public void process(Map<String, String> nodes) {
        for(Map.Entry<String,String> node : nodes.entrySet()){
            String path = node.getKey();
            String data = node.getValue();
            try {
                DataConversionFactory<EventLogModel> dataConversionFactory = new DataConversionFactory<EventLogModel>();
                IDataConversion<EventLogModel> dataConversion = dataConversionFactory.createDataConversion(LogDataConversion.class);
                EventLogModel eventLogModel = dataConversion.Decode(path, data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
