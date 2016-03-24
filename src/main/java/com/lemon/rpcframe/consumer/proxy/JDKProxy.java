package com.lemon.rpcframe.consumer.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.lemon.rpcframe.commons.GuiceDI;
import com.lemon.rpcframe.consumer.NettyClientProxy;
import com.lemon.rpcframe.consumer.registry.ConsumerManager;
import com.lemon.rpcframe.core.cluster.Config;
import com.lemon.rpcframe.core.loadbalance.RandomLoadBalance;
import com.lemon.rpcframe.io.netty5.MessageType;
import com.lemon.rpcframe.io.netty5.NettyMessage;
import com.lemon.rpcframe.util.GenericsUtils;
import com.lemon.rpcframe.util.SerializeUtil;

/**
 * 在原实现中，是直接调用的remoting的方法，没有使用代理
 * 
 * scf是实现的invocationhandler,然后通过这个进行调用
 * 
 * 
 * 
 * @author wangyazhou
 * @version 1.0
 * @date  2015年10月13日 下午2:51:21
 * @see 
 * @since
 */
public class JDKProxy {

    private static final Logger logger = Logger.getLogger(JDKProxy.class);
    public static final ConcurrentHashMap<String, Object> proxyMap = GenericsUtils.newConcurrentHashMap();
    //    public static final ConcurrentHashMap<String, Future<Object>> futureyMap = GenericsUtils.newConcurrentHashMap();
    public static final ExecutorService executor = Executors.newFixedThreadPool(6);

    private JDKProxy() {
        //禁止外部初始化
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static <T> T createProxy(final Class clazz, String str) {
        Object proxy = proxyMap.get(str);
        if (proxy != null) {
            return ((T) proxy);
        }

        logger.info(String.format("Create New JDK Proxy.", ""));
        proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[] { clazz }, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                NettyClientProxy clientProxy = GuiceDI.getInstance(NettyClientProxy.class);
                NettyMessage message = new NettyMessage();
                message.setType(MessageType.REMOTING_INVOKE.value());
                message.setIdentity(GuiceDI.getInstance(Config.class).getIdentity());
                message.setClazz(clazz.getName());
                message.setMethod(method.getName());
                //试图以此办法来序列化没有实现serializer接口的对象呢  但失败了
                //                Object[] newArgs = new Object[args.length];
                //                for (int i = 0; i < args.length; i++) {
                //                    Object arg = args[i];
                //                    if (arg.getClass().isAnnotationPresent(SCFSerializable.class)) {
                //                        newArgs[i] = SerializeUtil.jsonSerialize(arg);
                //                    } else {
                //                        newArgs[i] = arg;
                //                    }
                //                }
                message.setArgs(args);
                String address = GuiceDI.getInstance(RandomLoadBalance.class).select(GuiceDI.getInstance(ConsumerManager.class).getAddress(), "0");
                //                logger.info(String.format("request: %s", JSONObject.toJSONString(message)));
                NettyMessage resultMessage = clientProxy.getClient().invokeSync(address, message, 2000);
                //                logger.info(String.format("response: %s", JSONObject.toJSONString(message)));
                return SerializeUtil.jsonDeserialize(resultMessage.getResultJson());
            }
        });
        if (proxy != null) {
            proxyMap.put(str, proxy);
        }
        return (T) proxy;

    }
}
/**
 * 
        Future<Object> future = futureyMap.get(str);
        if (future == null) {
            future = executor.submit(new Callable() {
                @Override
                public Object call() throws Exception {
                    Object obj = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[] { clazz }, new InvocationHandler() {
                        @Override
                        public NettyMessage invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            NettyClientProxy clientProxy = InjectorHolder.getInstance(NettyClientProxy.class);
                            NettyMessage message = new NettyMessage();
                            message.setType(MessageType.TASK_PULL.value());
                            message.setNodeGroup("track_group");
                            message.setIdentity("test-task");
                            NettyMessage resultMessage = clientProxy.getClient().invokeSync("localhost:6061", message, 1000);
                            logger.info("jkdproxy invode");
                            return resultMessage;
                        }
                    });
                    return obj;
                }
            });
            futureyMap.put(str, future);
        } else {
            try {
                proxy = future.get(3000, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
            if (proxyMap.get(str) != null) {
                proxyMap.put(str, proxy);
            }
        }
        return (T) proxy;
 * 
 *  */

