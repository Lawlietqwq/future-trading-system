package com.csubigdata.futurestradingsystem.dao;

import com.csubigdata.futurestradingsystem.entity.StrategyToParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StrategyToParamsMapper {
    List<StrategyToParam> getStrategyParamById(@Param("strategyId") Integer strategyId);

    int insertStrategyParam(@Param("strategyId") Integer strategyId, StrategyToParam strategyToParam);


}
