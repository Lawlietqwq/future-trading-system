package com.csubigdata.futurestradingsystem.service;

import com.csubigdata.futurestradingsystem.entity.Position;
import com.csubigdata.futurestradingsystem.vo.PositionVO;

import java.util.List;

public interface PositionService {

    PositionVO getAllHoldingByModelId(int modelId);

    List<PositionVO> getAllHoldingByUid(int uid);

    List<PositionVO> getAllHoldingByCode(String code);

    int getOpenNumByModelId(int modelId);

    void insert(Position position);
}
