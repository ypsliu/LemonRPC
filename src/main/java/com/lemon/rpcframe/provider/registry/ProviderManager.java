package com.lemon.rpcframe.provider.registry;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.google.common.eventbus.Subscribe;
import com.google.inject.Singleton;
import com.lemon.rpcframe.commons.GuiceDI;
import com.lemon.rpcframe.core.cluster.Node;
import com.lemon.rpcframe.core.cluster.NodeType;
import com.lemon.rpcframe.provider.channel.ChannelManager;
import com.lemon.rpcframe.provider.channel.ChannelWrapper;
import com.lemon.rpcframe.provider.domain.ConsumerNode;
import com.lemon.rpcframe.util.ConcurrentHashSet;

/**
 * 管理所有的TaskNode节点
 *
 *
 * @author wangyazhou
 * @version 1.0
 * @date  2015年9月16日 下午7:47:15
 * @see 
 * @since
 */
@Singleton
public class ProviderManager {

    private static final Logger logger = Logger.getLogger(ProviderManager.class);

    private final ConcurrentHashMap<String/*nodeGroup*/, Set<ConsumerNode>> NODE_MAP = new ConcurrentHashMap<String, Set<ConsumerNode>>();

    public Set<String> getNodeGroups() {
        return NODE_MAP.keySet();
    }

    /**
     * 添加节点
     *
     * @param node
     */
    @Subscribe
    public void addNode(Node node) {
        //  channel 可能为 null
        logger.info("Add task node : " + node.toString());
        ChannelManager channelManager = GuiceDI.getInstance(ChannelManager.class);
        ChannelWrapper channel = channelManager.getChannel(node.getGroup(), node.getNodeType(), node.getIdentity());
        Set<ConsumerNode> taskTrackerNodes = NODE_MAP.get(node.getGroup());

        if (taskTrackerNodes == null) {
            taskTrackerNodes = new ConcurrentHashSet<ConsumerNode>();
            Set<ConsumerNode> oldSet = NODE_MAP.putIfAbsent(node.getGroup(), taskTrackerNodes);
            if (oldSet != null) {
                taskTrackerNodes = oldSet;
            }
        }

        ConsumerNode taskTrackerNode = new ConsumerNode(node.getGroup(), node.getThreads(), node.getIdentity(), channel);
        logger.info(String.format("Add TaskTracker node:%s", taskTrackerNode));
        taskTrackerNodes.add(taskTrackerNode);

    }

    /**
     * 删除节点
     *
     * @param node
     */
    @Subscribe
    public void removeNode(Node node) {
        Set<ConsumerNode> taskTrackerNodes = NODE_MAP.get(node.getGroup());
        if (taskTrackerNodes != null && taskTrackerNodes.size() != 0) {
            ConsumerNode taskTrackerNode = new ConsumerNode(node.getIdentity());
            taskTrackerNode.setNodeGroup(node.getGroup());
            logger.info(String.format("Remove TaskTracker node:%s", taskTrackerNode));
            taskTrackerNodes.remove(taskTrackerNode);
        }
    }

    public ConsumerNode getTaskTrackerNode(String nodeGroup, String identity) {
        Set<ConsumerNode> taskTrackerNodes = NODE_MAP.get(nodeGroup);
        if (taskTrackerNodes == null || taskTrackerNodes.size() == 0) {
            return null;
        }

        ChannelManager channelManager = GuiceDI.getInstance(ChannelManager.class);

        for (ConsumerNode taskTrackerNode : taskTrackerNodes) {
            if (taskTrackerNode.getIdentity().equals(identity)) {
                if (taskTrackerNode.getChannel() == null || taskTrackerNode.getChannel().isClosed()) {
                    // 如果 channel 已经关闭, 更新channel, 如果没有channel, 略过
                    ChannelWrapper channel = channelManager.getChannel(taskTrackerNode.getNodeGroup(), NodeType.CONSUMER_NODE, taskTrackerNode.getIdentity());
                    if (channel != null) {
                        // 更新channel
                        taskTrackerNode.setChannel(channel);
                        logger.info(String.format("update node channel , taskTackerNode=%s", taskTrackerNode));
                        return taskTrackerNode;
                    }
                } else {
                    // 只有当channel正常的时候才返回
                    return taskTrackerNode;
                }
            }
        }
        return null;
    }

}
