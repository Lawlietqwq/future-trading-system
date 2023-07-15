package com.csubigdata.futurestradingsystem.service;

import com.csubigdata.futurestradingsystem.entity.Strategy;
import com.csubigdata.futurestradingsystem.vo.CloseStrategyVO;
import com.csubigdata.futurestradingsystem.vo.OpenStrategyVO;
import org.apache.ibatis.annotations.Param;
import com.csubigdata.futurestradingsystem.vo.StrategyInfoVO;
import com.csubigdata.futurestradingsystem.vo.StrategyListVO;

import java.util.List;

public interface StrategyService {
    StrategyInfoVO getStrategyById(int strategyId);

    List<StrategyInfoVO> getAllStrategy();

    void updateStrategy(Strategy strategy);

    void insertStrategy(Strategy strategy);

    List<OpenStrategyVO> getAllOpenStrategy();

    List<CloseStrategyVO> getAllCloseStrategy();

}
