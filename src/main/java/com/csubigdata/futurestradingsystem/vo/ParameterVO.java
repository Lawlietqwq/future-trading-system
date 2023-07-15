package com.csubigdata.futurestradingsystem.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ParameterVO implements Serializable {

    private static final long serialVersionUID = 8515200897889727523L;

    private int paramId;

    private int modelId;

    private int strategyId;

    private String paramName;

    private double paramValue;
}
