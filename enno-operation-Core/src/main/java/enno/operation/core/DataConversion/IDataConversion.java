package enno.operation.core.DataConversion;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by v-zoli on 2015/11/11.
 */
public interface IDataConversion<T> {
    public String Encode(T model);
    public T Decode(String data);
}
