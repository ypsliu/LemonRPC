package com.lemon.rpcframe.core.loadbalance;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.google.inject.Singleton;

/**
 * 随机负载均衡算法 
 * 
 */
@Singleton
public class RandomLoadBalance extends AbstractLoadBalance {

    @Override
    protected <S> S doSelect(List<S> shards, String seed) {
        return shards.get(ThreadLocalRandom.current().nextInt(shards.size()));
    }
}
