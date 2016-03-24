package com.lemon.rpcframe.core.loadbalance;

import java.util.List;

/**
 * 负载均衡算法
 * 
 *
 * @author wangyazhou
 * @version 1.0
 * @date  2015年10月27日 下午2:07:46
 * @see 
 * @since
 */
public interface LoadBalance {

    public <S> S select(List<S> shards, String seed);

}
