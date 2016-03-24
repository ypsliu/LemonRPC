package com.lemon.rpcframe.core.failstore.leveldb;

import com.lemon.rpcframe.core.cluster.Config;
import com.lemon.rpcframe.core.failstore.FailStore;
import com.lemon.rpcframe.core.failstore.FailStoreFactory;
import com.lemon.rpcframe.util.StringUtils;

/**
 * Robert HG (254963746@qq.com) on 5/21/15.
 */
public class LeveldbFailStoreFactory implements FailStoreFactory {
    @Override
    public FailStore getFailStore(Config config, String storePath) {
        if (StringUtils.isEmpty(storePath)) {
            storePath = config.getFailStorePath();
        }
        return new LeveldbFailStore(storePath, config.getIdentity());
    }
}
