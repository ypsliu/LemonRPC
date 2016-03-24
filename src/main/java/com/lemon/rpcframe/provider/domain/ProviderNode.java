package com.lemon.rpcframe.provider.domain;

import com.lemon.rpcframe.core.cluster.Node;
import com.lemon.rpcframe.core.cluster.NodeType;

/**
 * @author Robert HG (254963746@qq.com) on 7/23/14.
 * Job Tracker 节点
 */
public class ProviderNode extends Node {

    public ProviderNode() {
        this.setNodeType(NodeType.CONSUMER_NODE);
    }
}
