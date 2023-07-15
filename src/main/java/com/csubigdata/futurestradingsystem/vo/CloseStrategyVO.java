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
public class CloseStrategyVO implements Serializable {

    private static final long serialVersionUID = 5980104264794350504L;

    private int closeId;

    private String closeName;

    private String closeClass;

    private List<StrategyToParam> closeParams;

}
