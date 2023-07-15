package com.csubigdata.futurestradingsystem.controller;

import com.csubigdata.futurestradingsystem.common.CommonException;
import com.csubigdata.futurestradingsystem.common.ResultTypeEnum;
import com.csubigdata.futurestradingsystem.entity.Strategy;
import com.csubigdata.futurestradingsystem.service.StrategyService;
import com.csubigdata.futurestradingsystem.common.Result;
import com.csubigdata.futurestradingsystem.util.BeanUtil;
import com.csubigdata.futurestradingsystem.vo.CloseStrategyVO;
import com.csubigdata.futurestradingsystem.vo.OpenStrategyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.csubigdata.futurestradingsystem.vo.StrategyInfoVO;
import com.csubigdata.futurestradingsystem.vo.StrategyListVO;

import java.util.List;

@RestController
@RequestMapping("/strategy")

public class StrategyController {

    @Autowired
    private StrategyService strategyService;

    @GetMapping("")
    public Result<List<StrategyInfoVO>> getAllStrategy() {
        Result<List<StrategyInfoVO>> result = new Result<>(ResultTypeEnum.SUCCESS);
        result.setData(strategyService.getAllStrategy());
        return result;
    }

    @GetMapping("/{strategyId}")
    public Result<StrategyInfoVO> getStrategyById(@PathVariable("strategyId") int strategyId) {
        StrategyInfoVO strategyInfoVO = strategyService.getStrategyById(strategyId);
        if(strategyInfoVO==null)
            CommonException.fail(ResultTypeEnum.QUERY_FAIL);
        Result<StrategyInfoVO> result = new Result<>(ResultTypeEnum.SUCCESS);
        result.setData(strategyInfoVO);
        return result;
    }

    @GetMapping("/open")
    public Result<List<OpenStrategyVO>> getAllOpenStrategy() {
        Result<List<OpenStrategyVO>> result = new Result<>(ResultTypeEnum.SUCCESS);
        result.setData(strategyService.getAllOpenStrategy());
        return result;
    }

    @GetMapping("/close")
    public Result<List<CloseStrategyVO>> getAllCloseStrategy() {
        Result<List<CloseStrategyVO>> result = new Result<>(ResultTypeEnum.SUCCESS);
        result.setData(strategyService.getAllCloseStrategy());
        return result;
    }

    @PostMapping("")
    public Result createStrategy(@RequestBody StrategyInfoVO strategyInfoVO){
        Strategy strategy=new Strategy();
        strategy = (Strategy) BeanUtil.copyProperties(strategyInfoVO,strategy);
        strategyService.insertStrategy(strategy);
        return new Result<>(ResultTypeEnum.SUCCESS);
    }

}
