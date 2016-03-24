package com.lemon.rpcframe.core.loadbalance;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.lemon.rpcframe.core.support.ConsistentHashSelector;

/**
 * 一致性hash算法 
 * 可做到
 * 
 * @author wangyazhou
 * @version 1.0
 * @date  2015年10月27日 下午2:10:42
 * @see 
 * @since
 */
public class ConsistentHashLoadBalance extends AbstractLoadBalance {

    @Override
    protected <S> S doSelect(List<S> shards, String seed) {
        if (seed == null || seed.length() == 0) {
            seed = "HASH-".concat(String.valueOf(ThreadLocalRandom.current().nextInt()));
        }
        ConsistentHashSelector<S> selector = new ConsistentHashSelector<S>(shards);
        return selector.selectForKey(seed);
    }
}
