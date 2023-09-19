package com.csubigdata.futurestradingsystem.service;

import com.csubigdata.futurestradingsystem.entity.Record;
import com.csubigdata.futurestradingsystem.vo.RecordVO;

import java.util.List;

public interface RecordService {

    /**
     * 根据用户id获取所有交易记录
     * @param uid 用户id
     * @return 交易记录信息组成的列表
     */
    List<RecordVO> getAllRecordByUid(int uid);

    /**
     * 根据模型id获取所有交易记录
     * @param uid 用户id
     * @param modelId 模型id
     * @return 模型id对应的交易记录组成的列表
     */
    List<RecordVO> getAllRecordByModelId(int uid, int modelId);

    /**
     * 根据期货代码获取交易记录
     * @param uid 用户id
     * @param code 期货代码
     * @return
     */
    List<RecordVO> getAllRecordByCode(int uid, String code);

    /**
     * 根据交易记录id删除交易记录
     * @param uid 用户id
     * @param recordId 交易记录id
     */
    void deleteByRecordId(int uid, int recordId);

    /**
     * 插入交易记录
     * @param record
     */
    void insert(Record record);
}
