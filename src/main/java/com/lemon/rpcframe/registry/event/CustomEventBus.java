package com.lemon.rpcframe.registry.event;

public interface CustomEventBus {

    public abstract void addNode(NodeAddEvent event);

    public abstract void removeNode(NodeRemoveEvent event);

}
