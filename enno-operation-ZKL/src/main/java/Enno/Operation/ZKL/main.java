package Enno.Operation.ZKL;

/**
 * Created by EriclLee on 15/11/10.
 */
public class main {
    private  static ZKClient zkClient;
    public static void main(String[] args)
    {
        System.out.println("Test");
        zkClient = new ZKClient();
        zkClient.setSubscriberNodeChangeEvent(new ZKLisener1());
        zkClient.setEventSourceChildNodeChangeEvent(new ZKLisener2());
        zkClient.start();
    }
}
