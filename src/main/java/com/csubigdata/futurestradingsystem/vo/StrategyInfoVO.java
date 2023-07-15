package com.csubigdata.futurestradingsystem.vo;

import com.csubigdata.futurestradingsystem.entity.StrategyToParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StrategyInfoVO implements Serializable {

    private static final long serialVersionUID = 154298943027612253L;

    private int strategyId;

    private String strategyName;

    private boolean openOrClose;

    private String remark;

    private List<StrategyToParam> strategyParamList;
}
