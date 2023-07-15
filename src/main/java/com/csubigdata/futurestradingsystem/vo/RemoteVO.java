package com.csubigdata.futurestradingsystem.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemoteVO implements Serializable {

    private static final long serialVersionUID = -867339133464867772L;

    private int uid;

    private int modelId;

    private String code;

    private String xinyiAccount;

    private String xinyiPwd;

    private String tradingAccount;

    private String tradingPwd;

    private String company;

    private boolean bkOrSk;

    private int lot;

}
