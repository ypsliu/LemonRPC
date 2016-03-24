package com.lemon.rpcframe.provider.registry;

import org.apache.log4j.Logger;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Singleton;
import com.lemon.rpcframe.commons.GuiceDI;
import com.lemon.rpcframe.core.cluster.Node;
import com.lemon.rpcframe.core.cluster.NodeType;
import com.lemon.rpcframe.registry.event.CustomEventBus;
import com.lemon.rpcframe.registry.event.NodeAddEvent;
import com.lemon.rpcframe.registry.event.NodeRemoveEvent;
import com.lemon.rpcframe.zookeeper.NodePathHelper;

/**
 * 监听Task节点的变化
 * 所有的节点存在TaskNodeManager中
 * 
 * @author wangyazhou
 * @version 1.0
 * @date  2015年9月16日 下午7:36:52
 * @see 
 * @since
 */
@Singleton
public class ProviderChangeBus implements CustomEventBus {

    private static final Logger logger = Logger.getLogger(ProviderChangeBus.class);

    @Subscribe
    public void addNode(NodeAddEvent event) {
        Preconditions.checkNotNull(event);
        Node node = NodePathHelper.parse(event.getPath());
        if (node.getNodeType().equals(NodeType.CONSUMER_NODE)) {
            //            logger.info(String.format("Add node %s %s", node.getGroup(), node.getIdentity()));
            ProviderManager taskManager = GuiceDI.getInstance(ProviderManager.class);
            taskManager.addNode(node);
        }
    }

    @Subscribe
    public void removeNode(NodeRemoveEvent event) {
        Preconditions.checkNotNull(event);
        Node node = NodePathHelper.parse(event.getPath());
        if (node.getNodeType().equals(NodeType.CONSUMER_NODE)) {
            //            logger.info(String.format("Delete node %s %s", node.getGroup(), node.getIdentity()));
            ProviderManager taskManager = GuiceDI.getInstance(ProviderManager.class);
            taskManager.removeNode(node);
        }
    }

}
