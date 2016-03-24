package com.lemon.rpcframe.registry;

import java.util.Set;

import org.apache.log4j.Logger;

import com.google.common.base.Preconditions;
import com.google.common.eventbus.EventBus;
import com.lemon.rpcframe.core.cluster.Node;
import com.lemon.rpcframe.registry.event.NodeAddEvent;
import com.lemon.rpcframe.registry.event.NodeRemoveEvent;
import com.lemon.rpcframe.util.ConcurrentHashSet;

/**
 * 事件注册中心  管理整个平台的事件信息
 * 为什么事件中心要放在最顶层啊?
 * 通过直接注册或节点变动产生的事件，最终由事件中心传播出去
 * @author wangyazhou
 * @version 1.0
 * @date  2015年9月21日 下午4:41:27
 * @see 
 * @since
 */
public abstract class EventPlatRegistry implements PlatRegistry {

    public static final Logger logger = Logger.getLogger(EventPlatRegistry.class);

    private final Set<Node> allNodes = new ConcurrentHashSet<Node>();

    private final EventBus registryBus = new EventBus("registry");

    @Override
    public void register(Node node) throws Exception {
        Preconditions.checkNotNull(node);
        allNodes.add(node);
    }

    @Override
    public void unregister(Node node) throws Exception {
        Preconditions.checkNotNull(node);
        allNodes.remove(node);
    }

    @Override
    public void subscribeChildChange(Node node, Object obj) throws Exception {
        Preconditions.checkNotNull(obj);
        registryBus.register(obj);
    }

    @Override
    public void unsubscribeChildChange(Node node, Object obj) throws Exception {
        Preconditions.checkNotNull(obj);
        registryBus.unregister(obj);
    }

    @Override
    public void childAdd(NodeAddEvent event) throws Exception {
        registryBus.post(event);
    }

    @Override
    public void childRemove(NodeRemoveEvent event) throws Exception {
        registryBus.post(event);
    }

    @Override
    public void destroy() throws Exception {
        allNodes.clear();
    }

    @Override
    public void recover() throws Exception {

    }

}
