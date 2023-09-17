package com.csubigdata.futurestradingsystem.service;

import com.csubigdata.futurestradingsystem.entity.Position;
import com.csubigdata.futurestradingsystem.vo.PositionVO;

import java.util.List;

public interface PositionService {

    /**
     * 根据模型id得到持仓
     * @param modelId 模型id
     * @return 返回持仓相关信息
     */
    PositionVO getAllHoldingByModelId(int modelId);

    /**
     * 获取用户的所有持仓
     * @param uid 用户id
     * @return 返回每个期货的持仓信息
     */
    List<PositionVO> getAllHoldingByUid(int uid);

    /**
     * 根据期货代码得到持仓信息
     * @param code 期货代码
     * @return 持仓组成的列表
     */
    List<PositionVO> getAllHoldingByCode(String code);

    /**
     * 根据模型id获取开仓数量
     * @param modelId 模型id
     * @return 返回开仓数量
     */
    int getOpenNumByModelId(int modelId);

    /**
     * 插入持仓信息
     * @param position 持仓信息
     */
    void insert(Position position);
}
