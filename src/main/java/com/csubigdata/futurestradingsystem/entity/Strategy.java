package com.csubigdata.futurestradingsystem.entity;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
public class Strategy {
    private int strategyId;

    private String strategyName;

    private boolean openOrClose;

    private String remark;

    private List<StrategyToParam> strategyParamList;

}