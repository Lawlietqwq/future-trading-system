package com.csubigdata.futurestradingsystem.service.impl;

import com.csubigdata.futurestradingsystem.common.CommonException;
import com.csubigdata.futurestradingsystem.common.ResultTypeEnum;
import com.csubigdata.futurestradingsystem.dao.RecordMapper;
import com.csubigdata.futurestradingsystem.entity.Record;
import com.csubigdata.futurestradingsystem.service.RecordService;
import com.csubigdata.futurestradingsystem.util.BeanUtil;
import com.csubigdata.futurestradingsystem.vo.ModelVO;
import com.csubigdata.futurestradingsystem.vo.RecordVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.csubigdata.futurestradingsystem.common.RedisCommonKey.MODEL_KEY_PREFIX;
import static com.csubigdata.futurestradingsystem.common.RedisCommonKey.TRADING_HISTORY_PREFIX;

@Service
public class RecordServiceImpl implements RecordService {

    @Autowired
    RecordMapper recordMapper;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Override
    public List<RecordVO> getAllRecordByUid(int uid) {
        String key = TRADING_HISTORY_PREFIX + uid;
        List<RecordVO> recordVOS = null;
        if(Boolean.TRUE.equals(stringRedisTemplate.hasKey(key))){
            HashOperations<String, Object, Object> stringObjectObjectHashOperations = stringRedisTemplate.opsForHash();
            List<Object> records = new ArrayList<>(stringObjectObjectHashOperations.entries(key).values());
            if (!CollectionUtils.isEmpty(records)) {
                recordVOS = BeanUtil.copyList(records, RecordVO.class);
                return recordVOS;
            }
            return null;
        }
        return recordMapper.getAllRecordByUid(uid);
    }

    @Override
    public List<RecordVO> getAllRecordByModelId(int uid, int modelId) {
        return recordMapper.getAllRecordByModelId(modelId);
    }

    @Override
    public List<RecordVO> getAllRecordByCode(int uid, String code) {
        String key = TRADING_HISTORY_PREFIX + uid;
        List<RecordVO> recordVOS = null;
        if(Boolean.TRUE.equals(stringRedisTemplate.hasKey(key))){
            HashOperations<String, Object, Object> stringObjectObjectHashOperations = stringRedisTemplate.opsForHash();
            List<Object> records = new ArrayList<>(stringObjectObjectHashOperations.entries(key).values());
            if (!CollectionUtils.isEmpty(records)) {
                recordVOS = BeanUtil.copyList(records, RecordVO.class);
                return recordVOS.stream().filter(each -> each.getCode().equals(code)).collect(Collectors.toList());
            }
            return null;
        }
        return recordMapper.getAllRecordByCode(code);
    }

    @Override
    public void deleteByRecordId(int uid, int recordId) {
        boolean success = recordMapper.deleteByRecordId(recordId) > 0;
        if (!success) CommonException.fail(ResultTypeEnum.DELETE_RECORD_ERROR);
        else stringRedisTemplate.opsForHash().delete(TRADING_HISTORY_PREFIX + uid, recordId);
    }

    @Override
    public void insert(Record record) {
        boolean success = recordMapper.insert(record) > 0;
        if (!success) CommonException.fail(ResultTypeEnum.INSERT_RECORD_ERROR);
        else stringRedisTemplate.opsForHash().put(TRADING_HISTORY_PREFIX + record.getUid(), record.getUid(), record);
    }
}
