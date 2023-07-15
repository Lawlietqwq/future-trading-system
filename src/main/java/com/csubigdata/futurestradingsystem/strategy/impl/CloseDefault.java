package com.csubigdata.futurestradingsystem.strategy.impl;

import com.csubigdata.futurestradingsystem.common.ModelStateEnum;
import com.csubigdata.futurestradingsystem.entity.Contract;
import com.csubigdata.futurestradingsystem.strategy.TradingExecutable;
import com.csubigdata.futurestradingsystem.vo.RedisContractVO;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CloseDefault implements TradingExecutable {

    @Override
    public void trade(RedisContractVO contract) {
    }

    @Override
    public void setLot(int lot) {
    }

    @Override
    public void setModelState(ModelStateEnum modelState) {

    }

    @Override
    public void clearData() {

    }
}
