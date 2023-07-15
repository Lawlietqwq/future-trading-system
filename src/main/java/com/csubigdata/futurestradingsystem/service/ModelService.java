package com.csubigdata.futurestradingsystem.service;

import com.csubigdata.futurestradingsystem.common.ModelStateEnum;
import com.csubigdata.futurestradingsystem.entity.Model;
import com.csubigdata.futurestradingsystem.vo.ModelVO;
import com.csubigdata.futurestradingsystem.vo.NewCloseStrategyVO;
import com.csubigdata.futurestradingsystem.vo.TradingVO;

import java.util.List;

public interface ModelService {


    List<ModelVO> getAll();

    List<ModelVO> getUserAllModel(int uid);

    ModelVO getModelById(int modelId);

    /**
     * 更新模型
     * @param model
     */
    void updateModelById(Model model);

    /**
     * 更新model state状态(即开启或结束状态)
     * @param modelId
     * @param modelState
     */
    void updateModelStateById(int modelId, ModelStateEnum modelState);

    void createModelFromOld(NewCloseStrategyVO newCloseStrategyVO);

    void createNewModel(Model model);

    void startModel(TradingVO tradingVO);

    void pauseModel(int modelId);

    void open(int modelId);

    void forceClose(TradingVO tradingVO);

    void successClose(int modelId);

    void deleteModel(int modelId);


}
