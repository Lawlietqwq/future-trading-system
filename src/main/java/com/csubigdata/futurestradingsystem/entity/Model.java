package com.csubigdata.futurestradingsystem.entity;

import com.csubigdata.futurestradingsystem.common.ModelStateEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;
import java.util.List;


@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Model {
    private Integer modelId;

    private String code;

    private Integer uid;

    private Integer openId;

    private Integer closeId;

    private Integer lot;

    private String openName;

    private String closeName;

    private String openClass;

    private String closeClass;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    private boolean bkOrSk;

    //true表示模型开始运行，false表示模型结束运行
    private ModelStateEnum modelState;

    private List<Parameter> openParams;

    private List<Parameter> closeParams;

}
