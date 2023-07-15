package com.csubigdata.futurestradingsystem.service.impl;

import com.csubigdata.futurestradingsystem.common.CommonException;
import com.csubigdata.futurestradingsystem.common.ResultTypeEnum;
import com.csubigdata.futurestradingsystem.dao.RecordMapper;
import com.csubigdata.futurestradingsystem.entity.Record;
import com.csubigdata.futurestradingsystem.service.RecordService;
import com.csubigdata.futurestradingsystem.vo.RecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordServiceImpl implements RecordService {

    @Autowired
    RecordMapper recordMapper;

    @Override
    public List<RecordVO> getAllRecordByUid(int uid) {
        return recordMapper.getAllRecordByUid(uid);
    }

    @Override
    public List<RecordVO> getAllRecordByModelId(int modelId) {
        return recordMapper.getAllRecordByModelId(modelId);
    }

    @Override
    public List<RecordVO> getAllRecordByCode(String code) {
        return recordMapper.getAllRecordByCode(code);
    }

    @Override
    public void deleteByRecordId(int recordId) {
        boolean success = recordMapper.deleteByRecordId(recordId) > 0;
        if (!success) CommonException.fail(ResultTypeEnum.DELETE_RECORD_ERROR);
    }

    @Override
    public void insert(Record record) {
        boolean success = recordMapper.insert(record) > 0;
        if (!success) CommonException.fail(ResultTypeEnum.INSERT_RECORD_ERROR);
    }
}
