package Enno.Operation.ZKL;

/**
 * Created by EriclLee on 15/11/9.
 */
import java.util.EventListener;
import java.util.Map;


public interface ZKListener extends EventListener {
    public void process(Map<String,String> nodes);
}