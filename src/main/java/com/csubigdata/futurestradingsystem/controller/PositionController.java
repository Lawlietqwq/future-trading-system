package com.csubigdata.futurestradingsystem.controller;


import com.csubigdata.futurestradingsystem.common.Result;
import com.csubigdata.futurestradingsystem.common.ResultTypeEnum;
import com.csubigdata.futurestradingsystem.service.PositionService;
import com.csubigdata.futurestradingsystem.vo.PositionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/position")
public class PositionController {

    @Autowired
    private PositionService positionService;

    @GetMapping("/model")
    public Result<PositionVO> getAllHoldingByModelId(int modelId){
        PositionVO positionVO = positionService.getAllHoldingByModelId(modelId);
        Result<PositionVO> result = new Result<>(ResultTypeEnum.SUCCESS);
        result.setData(positionVO);
        return result;
    }

    @GetMapping("/uid")
    public Result<List<PositionVO>> getAllHoldingByUid(int uid){
        List<PositionVO> positionVOList = positionService.getAllHoldingByUid(uid);
        Result<List<PositionVO>> result = new Result<>(ResultTypeEnum.SUCCESS);
        result.setData(positionVOList);
        return result;
    }

    @GetMapping("/code")
    public Result<List<PositionVO>> getAllHoldingByCode(String code){
        List<PositionVO> positionVOList = positionService.getAllHoldingByCode(code);
        Result<List<PositionVO>> result = new Result<>(ResultTypeEnum.SUCCESS);
        result.setData(positionVOList);
        return result;
    }

    @GetMapping("/{modelId}")
    public Result<Integer> getOpenNumByModelId(@PathVariable("modelId") int modelId){
        Result<Integer> result = new Result<>(ResultTypeEnum.SUCCESS);
        result.setData(positionService.getOpenNumByModelId(modelId));
        return result;
    }
}
