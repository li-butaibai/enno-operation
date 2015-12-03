package enno.operation.core.server;

import enno.operation.zkl.ZKAgentUtil;

/**
 * Created by saber on 2015/12/3.
 */
public class ZKAgent_Loader {
    public static void loadZKAgent() {
        ZKAgentUtil util = new ZKAgentUtil();
        for (int i = 0; i < 3; i++) {
            util.newZKAgentInstance("TestSub" + i, "For Test");
        }
    }
}
