package com.lemon.rpcframe.core.domain;

/**
 * @author Robert HG (254963746@qq.com) on 6/13/15.
 */
public enum Action {

    EXECUTE_SUCCESS("success"), // 执行成功,这种情况 直接反馈客户端
    EXECUTE_FAILED("failed"), // 执行失败,这种情况,直接反馈给客户端,不重新执行
    EXECUTE_LATER("later"), // 稍后重新执行,这种情况, 不反馈客户端,稍后重新执行,不过有最大重试次数
    EXECUTE_EXCEPTION("exception"); // 执行异常, 这中情况也会重试

    private String value;

    private Action(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
