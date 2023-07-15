package com.csubigdata.futurestradingsystem.dao;


import com.csubigdata.futurestradingsystem.entity.Position;
import com.csubigdata.futurestradingsystem.vo.PositionVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PositionMapper {

    PositionVO getAllHoldingByModelId(int modelId);

    List<PositionVO> getAllHoldingByUid(int uid);

    List<PositionVO> getAllHoldingByCode(String code);

    int getOpenNumByModelId(int modelId);

    int insert(Position position);

    int updateLotById(Integer modelId, Integer newModelId, int openNum);

    int deleteById(int modelId);
}
