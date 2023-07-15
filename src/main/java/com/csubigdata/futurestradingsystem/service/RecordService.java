package com.csubigdata.futurestradingsystem.service;

import com.csubigdata.futurestradingsystem.entity.Record;
import com.csubigdata.futurestradingsystem.vo.RecordVO;

import java.util.List;

public interface RecordService {

    List<RecordVO> getAllRecordByUid(int uid);

    List<RecordVO> getAllRecordByModelId(int modelId);

    List<RecordVO> getAllRecordByCode(String code);

    void deleteByRecordId(int recordId);

    void insert(Record record);
}
