package com.csubigdata.futurestradingsystem.dao;

import com.csubigdata.futurestradingsystem.entity.Strategy;
import com.csubigdata.futurestradingsystem.vo.CloseStrategyVO;
import com.csubigdata.futurestradingsystem.vo.OpenStrategyVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StrategyMapper {
    Strategy getById(Integer strategyId);

    List<Strategy> getAll();

    List<OpenStrategyVO> getAllOpenStrategy();

    List<CloseStrategyVO> getAllCloseStrategy();

    int insertStrategy(Strategy strategy);

    int insertSelective(Strategy strategy);

    int updateByIdSelective(Strategy strategy);

    int updateById(Strategy strategy);

}