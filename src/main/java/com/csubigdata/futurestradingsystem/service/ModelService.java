package com.csubigdata.futurestradingsystem.service;

import com.csubigdata.futurestradingsystem.common.ModelStateEnum;
import com.csubigdata.futurestradingsystem.entity.Model;
import com.csubigdata.futurestradingsystem.vo.ModelVO;
import com.csubigdata.futurestradingsystem.vo.NewCloseStrategyVO;
import com.csubigdata.futurestradingsystem.vo.TradingVO;

import java.util.List;

public interface ModelService {


    /**
     * 获取系统中的所有交易模型
     * @return 所有
     */
    List<ModelVO> getAll();

    /**
     * 获取用户创建的交易模型
     * @param uid 用户id
     * @return
     */
    List<ModelVO> getUserAllModel(int uid);

    /**
     * 得到某个模型的信息
     * @param modelId 交易模型id
     * @return
     */
    ModelVO getModelById(int modelId);

    /**
     * 更新模型
     * @param model 模型信息
     */
    void updateModelById(Model model);

    /**
     * 更新model state状态(即开启或结束状态)
     * @param modelId 交易模型id
     * @param modelState 交易模型状态
     */
    void updateModelStateById(int modelId, ModelStateEnum modelState);

    /**
     * 通过存在的旧交易模型创建新的交易模型
     * @param newCloseStrategyVO 新的平仓策略信息
     */
    void createModelFromOld(NewCloseStrategyVO newCloseStrategyVO);

    /**
     * 通过模型参数创建新的交易模型
     * @param model 交易模型的相关信息
     */
    void createNewModel(Model model);

    /**
     * 启动模型
     * @param tradingVO 交易模型的相关信息
     */
    void startModel(TradingVO tradingVO);

    /**
     * 暂停交易模型
     * @param modelId 模型id
     */
    void pauseModel(int modelId);

    /**
     * 执行开仓
     * @param modelId
     */
    void open(int modelId);

    /**
     * 强制平仓
     * @param tradingVO
     */
    void forceClose(TradingVO tradingVO);

    /**
     * 平仓成功
     * @param modelId
     */
    void successClose(int modelId);

    /**
     * 删除交易模型
     * @param modelId 模型id
     */
    void deleteModel(int modelId);


}
