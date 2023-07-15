package com.csubigdata.futurestradingsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpenStrategy {

    private int openId;

    private String openName;

    private String openClass;

    private List<StrategyToParam> openParams;

}
