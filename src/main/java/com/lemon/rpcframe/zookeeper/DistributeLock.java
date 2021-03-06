package com.lemon.rpcframe.zookeeper;

import org.apache.curator.framework.recipes.locks.InterProcessMutex;

import com.lemon.rpcframe.commons.GuiceDI;

/**
 * 分布式锁
 *
 *
 * @author wangyazhou
 * @version 1.0
 * @date  2015年9月22日 上午11:16:24
 * @see 
 * @since
 */
public class DistributeLock {

    /**
     * 获取分布式排它锁
     * @param path
     * @return
     */
    public static InterProcessMutex getInterProcessMutex(String node) {
        try {
            InterProcessMutex lock = new InterProcessMutex(GuiceDI.getInstance(CuratorZKClient.class).getClient(), NodePathHelper.getLockPath(node));
            return lock;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 释放锁资源 
     * @param lock
     */
    public static void release(InterProcessMutex lock) {
        if (null == lock) {
            return;
        }
        try {
            lock.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
