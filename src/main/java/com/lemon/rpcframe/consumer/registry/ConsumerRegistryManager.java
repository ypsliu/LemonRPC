package com.lemon.rpcframe.consumer.registry;

import org.apache.curator.utils.CloseableUtils;
import org.apache.log4j.Logger;

import com.google.inject.Singleton;
import com.lemon.rpcframe.commons.GuiceDI;
import com.lemon.rpcframe.consumer.Consumer;
import com.lemon.rpcframe.core.cluster.Config;
import com.lemon.rpcframe.core.factory.NodeFactory;
import com.lemon.rpcframe.provider.Damon;
import com.lemon.rpcframe.registry.CuratorPlateRegistry;
import com.lemon.rpcframe.util.Constants;
import com.lemon.rpcframe.zookeeper.CuratorZKClient;

/**
 * Task的注删监听类
 * 监听JOB节点的变化
 * 监听同TaskGroup节点的变化
 * 同组中 选出一个做Master
 * 
 * Task只需要关注
 * 
 * 
 * 原来的监听：
 * MasterChangeListenerImpl
 * SubscribedNodeManager
 * MasterElectionListener
 * SelfChangeListener
 * 
 * @author wangyazhou
 * @version 1.0
 * @date  2015年9月18日 下午3:24:47
 * @see 
 * @since
 */
@Singleton
public class ConsumerRegistryManager implements Damon {

    private static final Logger logger = Logger.getLogger(ConsumerRegistryManager.class);

    protected Consumer node;
    protected Config config;
    protected CuratorPlateRegistry registry;
    protected ConsumerMasterElector elector;

    public ConsumerRegistryManager() {
        config = GuiceDI.getInstance(Config.class);
        node = NodeFactory.create(Consumer.class, config);
        registry = new CuratorPlateRegistry();
        elector = new ConsumerMasterElector(GuiceDI.getInstance(CuratorZKClient.class).getClient(), "TASK");
        logger.info(String.format("%sTask Regist Init success %s", Constants.LOGTIP, Constants.LOGTIP));
    }

    @Override
    public void start() throws Exception {
        registry.register(node);
        elector.start();
        registry.subscribeChildChange(node, new ConsumerChangeBus());
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
