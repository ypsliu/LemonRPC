package com.lemon.rpcframe.util;

import java.util.Collection;

public class CollectionUtil {

    public static boolean isNotEmpty(Collection<?> collection) {
        return collection != null && collection.size() > 0;
    }
}
