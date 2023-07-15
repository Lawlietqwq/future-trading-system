package com.csubigdata.futurestradingsystem.entity;

import lombok.Data;

@Data
public class Parameter {
    private int paramId;

    private int modelId;

    private int strategyId;

    private String paramName;

    private double paramValue;
}
