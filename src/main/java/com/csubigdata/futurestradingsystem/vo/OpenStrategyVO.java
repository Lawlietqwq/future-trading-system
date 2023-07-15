package com.csubigdata.futurestradingsystem.vo;

import com.csubigdata.futurestradingsystem.entity.StrategyToParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpenStrategyVO implements Serializable {

    private static final long serialVersionUID = -3735479026265944586L;

    private int openId;

    private String openName;

    private String openClass;

    private List<StrategyToParam> openParams;

}