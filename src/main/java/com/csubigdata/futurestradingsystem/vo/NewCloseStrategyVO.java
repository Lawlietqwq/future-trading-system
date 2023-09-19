package com.csubigdata.futurestradingsystem.vo;


import com.csubigdata.futurestradingsystem.entity.Parameter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCloseStrategyVO implements Serializable {

    private static final long serialVersionUID = -3217122480017882131L;

    private int uid;

    private int modelId;

    private int closeId;

    private String closeName;

    private String closeClass;

    private List<Parameter> closeParams;

    private int lot;
}
