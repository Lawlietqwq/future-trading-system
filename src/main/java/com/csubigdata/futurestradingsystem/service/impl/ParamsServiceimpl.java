package com.csubigdata.futurestradingsystem.service.impl;

import com.csubigdata.futurestradingsystem.common.CommonException;
import com.csubigdata.futurestradingsystem.common.ResultTypeEnum;
import com.csubigdata.futurestradingsystem.dao.ParameterMapper;
import com.csubigdata.futurestradingsystem.dao.StrategyToParamsMapper;
import com.csubigdata.futurestradingsystem.entity.Parameter;
import com.csubigdata.futurestradingsystem.service.ParamsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ParamsServiceimpl implements ParamsService {

    @Autowired
    StrategyToParamsMapper strategyToParamsMapper;

    @Autowired
    ParameterMapper parameterMapper;
    @Override
    public List<Parameter> getParamsByStrategyId(int modelId, int strategyId) {
        List<Parameter> list = parameterMapper.getByModelIdAndStrategyId(modelId ,strategyId);
        return list;
    }

    @Override
    public boolean insertParams(Parameter parameter) {
        boolean success = parameterMapper.insertParams(parameter);
        if(!success) CommonException.fail(ResultTypeEnum.CREATE_PARAM_ERROR);
        return true;
    }

}
