package com.lemon.rpcframe.provider.handler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.lemon.rpcframe.commons.RemoteTaskService;
import com.lemon.rpcframe.util.GenericsUtils;

public class JobJDKProxy {

    private static final Logger logger = Logger.getLogger(JobJDKProxy.class);
    public static final ConcurrentHashMap<String, Object> proxyMap = GenericsUtils.newConcurrentHashMap();

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static <T> T createProxy(Class clazz, String str) {
        Object proxy = proxyMap.get(str);
        if (proxy != null) {
            return ((T) proxy);
        }
        logger.info("new proxy");
        //        RemoteTaskService target = new RemoteTaskServiceImpl();
        RemoteTaskService target = null; //获取这个接口的实现类
        proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[] { clazz }, new JobInvocationHandler(target));
        if (proxy != null) {
            proxyMap.put(str, proxy);
        }
        return (T) proxy;
    }

}

class JobInvocationHandler implements InvocationHandler {
    private Object target;

    public JobInvocationHandler(Object target) {
        super();
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return method.invoke(target, args);
    }

}
