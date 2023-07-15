package com.csubigdata.futurestradingsystem.vo;

import com.csubigdata.futurestradingsystem.entity.Parameter;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PositionVO implements Serializable {

    private static final long serialVersionUID = -8715270272901531321L;

    private int holdingId;

    private int uid;

    private int modelId;

    private int openId;

    private int closeId;

    private String openName;

    private String closeName;

    private List<Parameter> openParams;

    private List<Parameter> closeParams;

    private String code;

    private double openPrice;

    private int openNum;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date openTime;

    private boolean bkOrSk;

}
