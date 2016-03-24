package com.lemon.rpcframe.consumer;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.lemon.rpcframe.commons.GuiceDI;
import com.lemon.rpcframe.consumer.registry.ConsumerRegistryManager;
import com.lemon.rpcframe.core.cluster.Config;
import com.lemon.rpcframe.core.cluster.NodeType;
import com.lemon.rpcframe.provider.channel.ChannelManager;
import com.lemon.rpcframe.util.Constants;
import com.lemon.rpcframe.zookeeper.CuratorZKClient;

/**
 * 消费端consumeer的入口类
 * 
 * 
 * @author wangyazhou
 * @version 1.0
 * @date  2016年3月1日 下午5:18:38
 * @see 
 * @since
 */
public class ConsumerMain {

    private static final Logger logger = Logger.getLogger(ConsumerMain.class);

    private Config config;

    public static void main(String[] args) {
        final ConsumerMain task = new ConsumerMain();
        task.start();
    }

    public void start() {
        try {
            initConfig();//初始化配置
            initRegistry();//启动注册服务
            initCheck();//启动后台检查
            startNettyClient();//启动此服务
            addhook();
            logger.info(String.format("%sTask Client Start Success with identity : %s", Constants.LOGTIP, config.getIdentity()));
        } catch (Exception e) {
            try {
                stopAll();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } finally {
            logger.info("");
            logger.info(String.format("%sMay Job Be With You.%s", Constants.LOGTIP, Constants.LOGTIP));
        }
    }

    /**
     * 
     * 初始化配置文件
     * 初始化系统配置serverconfig
     * 初始化业务配置config
     */
    final private void initConfig() throws Exception {
        PropertyConfigurator.configure("D:/log4j.properties");
        //SCFInit.init("E:/opt/wf/com.bj58.zhaopin.web.foresee/scf.config");
        GuiceDI.init();
        logger.info("");
        config = GuiceDI.getInstance(Config.class);
        config.setIdentity("task-demo-3");
        config.setNodeType(NodeType.CONSUMER_NODE);
        GuiceDI.getInstance(CuratorZKClient.class);
        Thread.sleep(3000);
        logger.info(String.format("%sInit Config Success%s", Constants.LOGTIP, Constants.LOGTIP));
    }

    /**
     * 1 实现ZK的注册中心
     * 2 监听每个节点的变化
     * 3 监听同类别节点主从的变化
     * 4 清空僵尸任务  只能有
     * @throws Exception
     */
    final private void initRegistry() throws Exception {
        GuiceDI.getInstance(ConsumerRegistryManager.class).start();
        logger.info(String.format("%sInit Registry Success%s", Constants.LOGTIP, Constants.LOGTIP));
    }

    /**
     * 执行后台检查任务
     * 这个是任务一个Server都需要做的
     */
    final private void initCheck() throws Exception {
        GuiceDI.getInstance(ChannelManager.class).start();
        logger.info(String.format("%sInit Check Success%s", Constants.LOGTIP, Constants.LOGTIP));
    }

    /**
     * 启动服务
     */
    final private void startNettyClient() throws Exception {
        NettyClientProxy proxy = GuiceDI.getInstance(NettyClientProxy.class);
        proxy.init();
        logger.info(String.format("%sStart Netty Success%s", Constants.LOGTIP, Constants.LOGTIP));
    }

    final private void addhook() throws Exception {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    stopAll();
                } catch (Exception e) {
                    logger.error(e);
                }
            }
        }));
        logger.info(String.format("%sAdd Stophook %s", Constants.LOGTIP, Constants.LOGTIP));
    }

    final private void stopAll() throws Exception {

        logger.info(String.format("Task Client Stopped "));
    }

}
