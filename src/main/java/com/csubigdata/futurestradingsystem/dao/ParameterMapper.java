package com.csubigdata.futurestradingsystem.dao;

import com.csubigdata.futurestradingsystem.entity.Parameter;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParameterMapper {

    List<Parameter> getByModelIdAndStrategyId(Integer modelId , Integer strategyId);

    int deleteByModel(Integer modelId);

    boolean insertParams(Parameter parameter);

    int updateParamById(Parameter parameter);
}
