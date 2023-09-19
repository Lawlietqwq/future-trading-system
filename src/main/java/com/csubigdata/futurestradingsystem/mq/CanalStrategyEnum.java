package com.csubigdata.futurestradingsystem.mq;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Pattern;


/**
 * Canal 执行策略标记枚举
 *
 */
@RequiredArgsConstructor
public enum CanalStrategyEnum {

    /**
     * 模型表
     */
    T_MODEL("model"),

    /**
     * 交易记录表
     */
    T_TRADING("trading_history");

    @Getter
    private final String tabName;


}
