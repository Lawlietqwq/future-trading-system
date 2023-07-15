package com.csubigdata.futurestradingsystem.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class StrategyListVO implements Serializable {

    private static final long serialVersionUID = -4688246752901271029L;

    private int strategyId;

    private String strategyName;

}
