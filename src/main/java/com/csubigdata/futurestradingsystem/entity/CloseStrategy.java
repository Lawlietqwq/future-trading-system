package com.csubigdata.futurestradingsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CloseStrategy {

    private int closeId;

    private String closeName;

    private String closeClass;

    private List<StrategyToParam> closeParams;
}
