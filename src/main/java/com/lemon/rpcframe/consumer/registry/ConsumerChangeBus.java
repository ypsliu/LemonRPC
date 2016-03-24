package com.lemon.rpcframe.consumer.registry;

import org.apache.log4j.Logger;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.Subscribe;
import com.lemon.rpcframe.commons.GuiceDI;
import com.lemon.rpcframe.core.cluster.Node;
import com.lemon.rpcframe.core.cluster.NodeType;
import com.lemon.rpcframe.registry.event.CustomEventBus;
import com.lemon.rpcframe.registry.event.NodeAddEvent;
import com.lemon.rpcframe.registry.event.NodeRemoveEvent;
import com.lemon.rpcframe.zookeeper.NodePathHelper;

public class ConsumerChangeBus implements CustomEventBus {

    private static final Logger logger = Logger.getLogger(ConsumerChangeBus.class);

    @Subscribe
    @Override
    public void addNode(NodeAddEvent event) {
        Preconditions.checkNotNull(event);
        Node node = NodePathHelper.parse(event.getPath());
        if (node.getNodeType().equals(NodeType.PROVIDER_NODE)) {
            logger.info(String.format("Add node %s %s", node.getGroup(), node.getIdentity()));
            ConsumerManager jobManager = GuiceDI.getInstance(ConsumerManager.class);
            jobManager.addNode(node);
        }
    }

    @Subscribe
    @Override
    public void removeNode(NodeRemoveEvent event) {
        Preconditions.checkNotNull(event);
        Node node = NodePathHelper.parse(event.getPath());
        if (node.getNodeType().equals(NodeType.PROVIDER_NODE)) {
            logger.info(String.format("Delete node %s %s", node.getGroup(), node.getIdentity()));
            ConsumerManager jobManager = GuiceDI.getInstance(ConsumerManager.class);
            jobManager.removeNode(node);
        }
    }

}
