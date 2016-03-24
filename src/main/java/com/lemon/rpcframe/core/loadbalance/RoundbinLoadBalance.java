package com.lemon.rpcframe.core.loadbalance;

import java.util.List;

/**
 * 轮询调度调度算法
 * 以轮询的方式依次将请求调度不同的服务器
 *
 * @author wangyazhou
 * @version 1.0
 * @date  2015年10月27日 下午2:06:44
 * @see 
 * @since
 */
public class RoundbinLoadBalance extends AbstractLoadBalance {
    @Override
    protected <S> S doSelect(List<S> shards, String seed) {
        // TODO
        return null;
    }
}
