import enno.operation.zkl.ZKAgent;
import enno.operation.zkl.ZKAgentUtil;

/**
 * Created by sabermai on 2015/12/1.
 */
public class Main {
    public static void main(String[] args) {
        ZKAgentUtil util = new ZKAgentUtil();
        for (int i = 0; i < 3; i++) {
            util.newZKAgentInstance("TestSub" + i);
        }
    }
}
