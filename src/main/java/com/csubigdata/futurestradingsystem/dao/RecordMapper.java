package com.csubigdata.futurestradingsystem.dao;

import com.csubigdata.futurestradingsystem.entity.Record;
import com.csubigdata.futurestradingsystem.vo.RecordVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordMapper {

    List<RecordVO> getAllRecordByUid(int uid);

    List<RecordVO> getAllRecordByModelId(int modelId);

    List<RecordVO> getAllRecordByCode(String code);

    int deleteByRecordId(int recordId);

    int insert(Record record);
}