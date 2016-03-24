package com.lemon.rpcframe.core.loadbalance;

import java.util.List;

/**
 * 公共抽象类，可提前做过滤
 *
 *
 * @author wangyazhou
 * @version 1.0
 * @date  2015年10月27日 下午2:07:57
 * @see 
 * @since
 */
public abstract class AbstractLoadBalance implements LoadBalance {

    @Override
    public <S> S select(List<S> shards, String seed) {
        if (shards == null || shards.size() == 0) {
            return null;
        }

        if (shards.size() == 1) {
            return shards.get(0);
        }

        return doSelect(shards, seed);
    }

    protected abstract <S> S doSelect(List<S> shards, String seed);

}
