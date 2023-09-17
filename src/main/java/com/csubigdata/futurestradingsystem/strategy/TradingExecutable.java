package com.csubigdata.futurestradingsystem.strategy;

import com.csubigdata.futurestradingsystem.common.ModelStateEnum;
import com.csubigdata.futurestradingsystem.entity.Contract;
import com.csubigdata.futurestradingsystem.vo.RedisContractVO;

public interface TradingExecutable {

    /**
     * 计算交易信号，如果交易信号产生执行开仓
     * @param contract 合约信息
     */
    void trade(RedisContractVO contract);

    /**
     * 设置交易的手数
     * @param lot 手数
     */
    void setLot(int lot);

    /**
     * 设置交易模型的交易状态
     * @param modelState 模型的交易状态相关信息
     */
    void setModelState(ModelStateEnum modelState);

    /**
     * 清空数据
     */
    void clearData();
}
