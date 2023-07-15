package com.csubigdata.futurestradingsystem.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContractVO implements Serializable {

    private static final long serialVersionUID = -1111255161403413332L;

    private String code;

    private double open;

    private double close;

    private double low;

    private double high;

    private double vol;

    private double amount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date tradeDate;

}
