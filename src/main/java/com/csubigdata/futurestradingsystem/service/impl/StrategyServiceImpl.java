package com.csubigdata.futurestradingsystem.service.impl;

import com.csubigdata.futurestradingsystem.common.CommonException;
import com.csubigdata.futurestradingsystem.common.ResultTypeEnum;
import com.csubigdata.futurestradingsystem.dao.StrategyMapper;
import com.csubigdata.futurestradingsystem.dao.StrategyToParamsMapper;
import com.csubigdata.futurestradingsystem.entity.Strategy;
import com.csubigdata.futurestradingsystem.entity.StrategyToParam;
import com.csubigdata.futurestradingsystem.service.StrategyService;
import com.csubigdata.futurestradingsystem.util.BeanUtil;
import com.csubigdata.futurestradingsystem.vo.CloseStrategyVO;
import com.csubigdata.futurestradingsystem.vo.OpenStrategyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import com.csubigdata.futurestradingsystem.vo.StrategyInfoVO;
import com.csubigdata.futurestradingsystem.vo.StrategyListVO;

import java.util.ArrayList;
import java.util.List;

@Service
public class StrategyServiceImpl implements StrategyService {
    @Autowired
    private StrategyMapper strategyMapper;

    @Autowired
    private StrategyToParamsMapper strategyToParamsMapper;

    @Override
    public StrategyInfoVO getStrategyById(int strategyId) {
        Strategy strategy = strategyMapper.getById(strategyId);
        StrategyInfoVO strategyInfoVO = new StrategyInfoVO();
        if (strategy == null) {
            return null;
        } else {
            strategyInfoVO = (StrategyInfoVO) BeanUtil.copyProperties(strategy,strategyInfoVO);
        }
        return strategyInfoVO;
    }

    @Override
    public List<StrategyInfoVO> getAllStrategy() {
        List<Strategy> strategyList = strategyMapper.getAll();
        List<StrategyInfoVO> strategyInfoVOS = new ArrayList<>();
        if (!CollectionUtils.isEmpty(strategyList)) {
            strategyInfoVOS = BeanUtil.copyList(strategyList, StrategyInfoVO.class);
        }else return null;
        return strategyInfoVOS;
    }

    @Override
    public void updateStrategy(Strategy strategy) {

    }

    @Override
    @Transactional(rollbackFor = CommonException.class)
    public void insertStrategy(Strategy strategy) {
        boolean successStrategy = strategyMapper.insertStrategy(strategy)>0;
        Integer strategyId = strategy.getStrategyId();
        List<StrategyToParam> strategyParamList = strategy.getStrategyParamList();
        for (StrategyToParam param : strategyParamList)
        {
            strategyToParamsMapper.insertStrategyParam(strategyId, param);
        }
        if (!successStrategy) CommonException.fail(ResultTypeEnum.CREATE_MODEL_ERROR);
    }

    @Override
    public List<OpenStrategyVO> getAllOpenStrategy() {
        List<OpenStrategyVO> openStrategyVOList = strategyMapper.getAllOpenStrategy();
        return openStrategyVOList;
    }

    @Override
    public List<CloseStrategyVO> getAllCloseStrategy() {
        List<CloseStrategyVO> closeStrategyVOList = strategyMapper.getAllCloseStrategy();
        return closeStrategyVOList;
    }
}
