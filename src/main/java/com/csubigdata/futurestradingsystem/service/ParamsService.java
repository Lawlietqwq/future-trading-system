package com.csubigdata.futurestradingsystem.service;

import com.csubigdata.futurestradingsystem.entity.Parameter;

import java.util.List;

public interface ParamsService {
    /**
     * 根据模型id和策略id获取策略参数
     * @param modelId 模型id
     * @param strategyId 策略id
     * @return 返回参数类组成的列表
     */
    List<Parameter> getParamsByStrategyId(int modelId, int strategyId);

    /**
     * 插入参数
     * @param parameter 参数类
     * @return 是否插入成功
     */
    boolean insertParams(Parameter parameter);
}
