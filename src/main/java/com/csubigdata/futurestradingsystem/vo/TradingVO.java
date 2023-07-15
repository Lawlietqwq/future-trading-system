package com.csubigdata.futurestradingsystem.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradingVO implements Serializable {

    private static final long serialVersionUID = 3568199404499274394L;

    private Integer modelId;

    private String xinyiAccount;

    private String xinyiPwd;

    private String tradingAccount;

    private String tradingPwd;

    private String company;

    private int lot;
}
