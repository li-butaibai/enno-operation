import enno.operation.zkl.ZKClient;

/**
 * Created by EriclLee on 15/11/10.
 */
public class TestStart {
    private static ZKClient zkClient= new ZKClient();
    public static void main(String[] args )
    {
        zkClient.setConnectString("127.0.0.1:2181");
        
    }
}
