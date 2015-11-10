package Enno.Operation.ZKL;

import java.util.Map;

/**
 * Created by EriclLee on 15/11/10.
 */
public class ZKLisener1 implements ZKListener
{
    public void process(Map<String,String> nodes)
    {
        System.out.println("ZKListener1");
    }
}
