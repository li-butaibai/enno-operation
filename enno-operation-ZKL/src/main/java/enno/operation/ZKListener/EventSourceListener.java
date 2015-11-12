package enno.operation.ZKListener;

import enno.operation.zkmodel.EventSourceData;

import java.util.EventListener;

/**
 * Created by v-zoli on 2015/11/12.
 */
public interface EventSourceListener extends EventListener {
public void process(EventSourceData eventSourceData);
}
