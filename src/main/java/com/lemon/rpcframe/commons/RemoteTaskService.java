package com.lemon.rpcframe.commons;

/**
 * 远程调用接口
 * 客户端的实现使用代理NETY的方式
 * 服务端的实现使用scf查询数据库的方式
 * 
 * @author wangyazhou
 * @version 1.0 
 * @date   2015年10月13日 下午2:12:02
 * @see    
 * @since  
 */
public interface RemoteTaskService {

    /**
     * 新增一个节点组 主要是taskgroup多些
     * @param nodeType
     * @param nodeGroup
     * @return
     * @throws Exception
     */
    public abstract String test(String nodeType, String nodeGroup) throws Exception;

}
