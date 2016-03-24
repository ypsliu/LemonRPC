package com.lemon.rpcframe.provider.registry;

import org.apache.curator.utils.CloseableUtils;
import org.apache.log4j.Logger;

import com.google.inject.Singleton;
import com.lemon.rpcframe.commons.GuiceDI;
import com.lemon.rpcframe.core.cluster.Config;
import com.lemon.rpcframe.core.factory.NodeFactory;
import com.lemon.rpcframe.provider.Damon;
import com.lemon.rpcframe.provider.domain.ProviderNode;
import com.lemon.rpcframe.registry.CuratorPlateRegistry;
import com.lemon.rpcframe.util.Constants;
import com.lemon.rpcframe.zookeeper.CuratorZKClient;

/**
 * 管理server端的节点变更 和 Master变更
 * 
 * 
 * 原JOB订阅了  感觉好重啊
 * MasterChangeListenerImpl
 * JobNodeChangeListener   以组的形式  管理task 和 client  并做一些检查性工作    只有job加了
 * JobTrackerMasterChangeListener
 * SubscribedNodeManager   以类型的形式  对所有的节点进行管理 
 * MasterElectionListener
 * SelfChangeListener
 * 
 * 
 * @author wangyazhou
 * @version 1.0
 * @date  2015年9月16日 下午7:25:19
 * @see 
 * @since
 */
@Singleton
public class ProviderRegistryManager implements Damon {

    private static final Logger logger = Logger.getLogger(ProviderRegistryManager.class);

    protected ProviderNode node;
    protected Config config;
    protected CuratorPlateRegistry registry;
    protected ProviderMasterElector elector;

    //    protected EventBus nodeChangeEventBus;//事件通知类

    public ProviderRegistryManager() {
        config = GuiceDI.getInstance(Config.class);
        node = NodeFactory.create(ProviderNode.class, config);
        registry = new CuratorPlateRegistry();
        elector = new ProviderMasterElector(GuiceDI.getInstance(CuratorZKClient.class).getClient(), "JOB");
    }

    @Override
    public void start() throws Exception {
        registry.register(node);
        elector.start();
        registry.subscribeChildChange(node, new ProviderChangeBus());
        //        registry.subscribeChange(node, new SubscribedNodeManager());
        //        registry.subscribeChange(node, new SelfChangeListener());
        logger.info(String.format("%sJob Registry Init Success%s ", Constants.LOGTIP, Constants.LOGTIP));
    }

    @Override
    public void stop() throws Exception {
        if (registry != null) {
            registry.unregister(node);
            registry.destroy();
        }
        CloseableUtils.closeQuietly(elector);
    }

}
