package com.csubigdata.futurestradingsystem.patterns;

/**
 * 策略执行抽象
 *
 */
public interface AbstractExecuteStrategy<REQ, RES> {

    /**
     * 执行策略标识
     */
    default String mark() {
        return null;
    }

    /**
     * 执行策略
     *
     * @param requestParam 执行策略入参
     */
    default void execute(REQ requestParam) {

    }

    /**
     * 执行策略，带返回值
     *
     * @param requestParam 执行策略入参
     * @return 执行策略后返回值
     */
    default RES executeResp(REQ requestParam) {
        return null;
    }
}
