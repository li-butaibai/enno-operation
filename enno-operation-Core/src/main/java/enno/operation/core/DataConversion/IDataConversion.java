package enno.operation.core.DataConversion;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.List;
import java.util.Map;

/**
 * Created by v-zoli on 2015/11/11.
 */
public interface IDataConversion<T> {
    public String Encode(T model);
    public T Decode(String path, String data);
    public List<T> Decode(Map<String,String> nodes);
}
