package com.csubigdata.futurestradingsystem.controller;

import com.csubigdata.futurestradingsystem.common.RepeatSubmit;
import com.csubigdata.futurestradingsystem.common.Result;
import com.csubigdata.futurestradingsystem.common.ResultTypeEnum;
import com.csubigdata.futurestradingsystem.entity.Record;
import com.csubigdata.futurestradingsystem.service.RecordService;
import com.csubigdata.futurestradingsystem.vo.RecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/record")
public class RecordController {

    @Autowired
    RecordService recordService;

    @GetMapping("/uid")
    Result<List<RecordVO>> getAllRecordByUid(int uid){
        List<RecordVO> record = recordService.getAllRecordByUid(uid);
        Result<List<RecordVO>> result = new Result<>(ResultTypeEnum.SUCCESS);
        result.setData(record);
        return result;
    }

    @GetMapping("/model")
    Result<List<RecordVO>> getAllRecordByModelId(int modelId){
        List<RecordVO> record = recordService.getAllRecordByModelId(modelId);
        Result<List<RecordVO>> result = new Result<>(ResultTypeEnum.SUCCESS);
        result.setData(record);
        return result;
    }

    @GetMapping("/code")
    Result<List<RecordVO>> getAllRecordByCode(String code){
        List<RecordVO> record = recordService.getAllRecordByCode(code);
        Result<List<RecordVO>> result = new Result<>(ResultTypeEnum.SUCCESS);
        result.setData(record);
        return result;
    }

    @DeleteMapping("/{recordId}")
    Result deleteByRecordId(@PathVariable("recordId") int recordId){
        recordService.deleteByRecordId(recordId);
        return new Result(ResultTypeEnum.SUCCESS);
    }

    @PostMapping("")
    @RepeatSubmit
    Result insert(@RequestBody Record record){
        recordService.insert(record);
        return new Result(ResultTypeEnum.SUCCESS);
    }

}
