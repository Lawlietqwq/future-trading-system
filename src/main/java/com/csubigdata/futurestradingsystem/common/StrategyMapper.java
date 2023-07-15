package com.csubigdata.futurestradingsystem.common;

public enum StrategyMapper {

    OpenMA(1),
    CloseMA(2),
    OpenBoll(3),
    CloseBoll(4);

    private int strategyId;

    StrategyMapper(int strategyId){
        this.strategyId = strategyId;
    }

    public int getStrategyId(){
        return strategyId;
    }

    public void  setStrategyId(int strategyId){
        this.strategyId = strategyId;
    }


    @Override
    public String toString() {
        return "StrategyMapper{" +
                "strategyId='" + strategyId + '\'' +
                '}';
    }
}
