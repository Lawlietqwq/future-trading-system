package com.csubigdata.futurestradingsystem.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RedisContractVO implements Serializable {

    private static final long serialVersionUID = -2772917454791960212L;

    private String code;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date trade_date;

    private double open;

    private double close;

    private double low;

    private double high;

    private double vol;

    private double amount;

    private boolean min_started;
}
