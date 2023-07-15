package com.csubigdata.futurestradingsystem.dto;

import com.csubigdata.futurestradingsystem.common.ModelStateEnum;
import com.csubigdata.futurestradingsystem.entity.Parameter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ModelInstanceDTO {

    private String code;

    private int uid;

    private int modelId;

    private String openClass;

    private String closeClass;

    private boolean bkOrSk;

    private int lot;

    private ModelStateEnum modelState;

    private String xinyiAccount;

    private String xinyiPwd;

    private String tradingAccount;

    private String tradingPwd;

    private String company;

    private List<Parameter> openParams;

    private List<Parameter> closeParams;
}
