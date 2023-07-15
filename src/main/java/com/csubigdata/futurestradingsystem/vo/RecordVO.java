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
public class RecordVO implements Serializable {

    private static final long serialVersionUID = 5635676857284047664L;

    private int recordId;

    private String openName;

    private String closeName;

    private List<Parameter> openParams;

    private List<Parameter> closeParams;

    private int uid;

    private int modelId;

    private String code;

    private boolean bkOrSk;

    private int lot;

    private double openPrice;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date openTime;

    private double closePrice;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date closeTime;

    private double profit;

}
