package com.lemon.rpcframe.core.loadbalance;

import java.util.List;

public class PerformanceLoadBalance extends AbstractLoadBalance {

    @Override
    protected <S> S doSelect(List<S> shards, String seed) {
        return null;
    }
    
}
