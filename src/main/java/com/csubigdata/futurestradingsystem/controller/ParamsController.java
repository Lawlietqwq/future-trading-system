package com.csubigdata.futurestradingsystem.controller;

import com.csubigdata.futurestradingsystem.common.Result;
import com.csubigdata.futurestradingsystem.common.ResultTypeEnum;
import com.csubigdata.futurestradingsystem.entity.Parameter;
import com.csubigdata.futurestradingsystem.service.ParamsService;
import com.csubigdata.futurestradingsystem.vo.ModelVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 返回策略超参
 */
@RestController
public class ParamsController {
    @Autowired
    private ParamsService paramsService;


    /**
     * 返回模型的参数
     */
    @GetMapping("/params")
    Result<List<Parameter>> getModelParams(HttpServletRequest request){
        Result<List<Parameter>> result = new Result<>(ResultTypeEnum.SUCCESS);
        result.setData(paramsService.getParamsByStrategyId(Integer.parseInt(request.getParameter("modelId")), Integer.parseInt(request.getParameter("strategyId"))));
        return result;
    }

}
