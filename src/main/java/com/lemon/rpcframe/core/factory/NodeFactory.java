package com.lemon.rpcframe.core.factory;

import com.lemon.rpcframe.core.cluster.Config;
import com.lemon.rpcframe.core.cluster.Node;

/**
 * @author Robert HG (254963746@qq.com) on 7/25/14. 节点工厂类
 */
public class NodeFactory {

    public static <T extends Node> T create(Class<T> clazz, Config config) {
        try {
            T node = clazz.newInstance();
            //            node.setIp(NetUtils.getLocalHost());
            node.setGroup(config.getNodeGroup());
            node.setThreads(config.getWorkThreads());
            node.setPort(config.getListenPort());
            node.setIdentity(config.getIdentity());
            node.setClusterName(config.getClusterName());
            return node;
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
