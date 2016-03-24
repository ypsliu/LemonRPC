package com.lemon.rpcframe.consumer.registry;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Singleton;
import com.lemon.rpcframe.core.cluster.Node;

@Singleton
public class ConsumerManager {

    private static final Logger logger = Logger.getLogger(ConsumerManager.class);

    private static final Set<Node> nodeSet = new HashSet<Node>();
    private static final List<String> addresslist = new ArrayList<String>();

    @Subscribe
    public void addNode(Node node) {
        nodeSet.add(node);
        addresslist.add(node.getAddress());
        logger.info("Add job Node " + node.toString());
    }

    @Subscribe
    public void removeNode(Node node) {
        nodeSet.remove(node);
        addresslist.remove(node.getAddress());
        logger.info("Remove job Node " + node.getIdentity());

    }

    public static Set<Node> allJobNode() {
        return nodeSet;
    }

    public static List<String> getAddress() {
        return addresslist;
    }

}
