package com.csubigdata.futurestradingsystem.strategy;

import com.csubigdata.futurestradingsystem.common.ModelStateEnum;
import com.csubigdata.futurestradingsystem.entity.Contract;
import com.csubigdata.futurestradingsystem.vo.RedisContractVO;

public interface TradingExecutable {

    void trade(RedisContractVO contract);

    void setLot(int lot);

    void setModelState(ModelStateEnum modelState);

    void clearData();
}
