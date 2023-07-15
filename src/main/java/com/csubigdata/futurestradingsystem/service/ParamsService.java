package com.csubigdata.futurestradingsystem.service;

import com.csubigdata.futurestradingsystem.entity.Parameter;

import java.util.List;

public interface ParamsService {
    List<Parameter> getParamsByStrategyId(int modelId, int strategyId);

    boolean insertParams(Parameter parameter);
}
