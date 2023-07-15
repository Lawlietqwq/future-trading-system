package com.csubigdata.futurestradingsystem.service.impl;

import com.csubigdata.futurestradingsystem.dao.PositionMapper;
import com.csubigdata.futurestradingsystem.entity.Position;
import com.csubigdata.futurestradingsystem.service.PositionService;
import com.csubigdata.futurestradingsystem.strategy.AsyncTask;
import com.csubigdata.futurestradingsystem.vo.PositionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PositionServiceImpl implements PositionService {

    @Autowired
    private PositionMapper positionMapper;

    @Override
    public PositionVO getAllHoldingByModelId(int modelId) {
        return positionMapper.getAllHoldingByModelId(modelId);
    }

    @Override
    public List<PositionVO> getAllHoldingByUid(int uid) {
        return positionMapper.getAllHoldingByUid(uid);
    }

    @Override
    public List<PositionVO> getAllHoldingByCode(String code) {
        return positionMapper.getAllHoldingByCode(code);
    }

    @Override
    public int getOpenNumByModelId(int modelId) {
        if (AsyncTask.getModelInstanceMap().containsKey(modelId) && !AsyncTask.getModelInstanceMap().get(modelId).isFinished())
            return positionMapper.getOpenNumByModelId(modelId);
        else return 0;
    }

    @Override
    public void insert(Position position) {

    }
}
