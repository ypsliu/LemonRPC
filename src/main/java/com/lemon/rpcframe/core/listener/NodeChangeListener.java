package com.lemon.rpcframe.core.listener;

import java.util.List;

import com.lemon.rpcframe.core.cluster.Node;

/**
 * 对服务节点来说 只有这两种事件
 * 不会有更新
 */
public interface NodeChangeListener {

    /**
     * 添加节点
     *
     * @param nodes
     */
    public void addNodes(List<Node> nodes);

    /**
     * 移除节点
     * 
     * @param nodes
     */
    public void removeNodes(List<Node> nodes);

}
