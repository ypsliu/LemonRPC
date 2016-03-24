package com.lemon.rpcframe.consumer.registry;

import java.io.Closeable;
import java.io.IOException;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.log4j.Logger;

import com.lemon.rpcframe.commons.GuiceDI;
import com.lemon.rpcframe.core.cluster.Config;
import com.lemon.rpcframe.util.Constants;

/**
 * 对于周期性任务 MASTER节点可以做些周期性检查  这样又可以避免THREAD.SLEEP(XXX) 带来的不可控问题
 * 对于实时任务  Master是主要的执行节点
 *
 *
 * @author wangyazhou
 * @version 1.0
 * @date  2015年10月26日 下午8:01:54
 * @see 
 * @since
 */
public class ConsumerMasterElector extends LeaderSelectorListenerAdapter implements Closeable {

    private static final Logger logger = Logger.getLogger(ConsumerMasterElector.class);

    private final LeaderSelector leaderSelector;

    public ConsumerMasterElector(CuratorFramework client, String node) {
        leaderSelector = new LeaderSelector(client, getRealPath(node), this);
        leaderSelector.autoRequeue();
    }

    public void start() throws Exception {
        leaderSelector.start();
    }

    @Override
    public void takeLeadership(CuratorFramework arg0) throws Exception {
        Config config = GuiceDI.getInstance(Config.class);
        logger.info(String.format("%s%s %s take leader ship", Constants.LOGTIP, config.getNodeGroup(), config.getIdentity()));
        Thread.sleep(Integer.MAX_VALUE);
    }

    private static String getRealPath(String node) {
        return String.format("/%s/%s/%s", GuiceDI.getInstance(Config.class).getClusterName(), "Electors", node);
    }

    @Override
    public void close() throws IOException {
        logger.info(String.format("JobMasterElector close", ""));
        leaderSelector.close();
    }

    @Override
    public void stateChanged(CuratorFramework client, ConnectionState newState) {
        if ((newState == ConnectionState.SUSPENDED) || (newState == ConnectionState.LOST)) {//失去leader关系
            try {
                Config config = GuiceDI.getInstance(Config.class);
                logger.info(String.format("%s%s-%s lose leader ship", Constants.LOGTIP, config.getNodeGroup(), config.getIdentity()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
