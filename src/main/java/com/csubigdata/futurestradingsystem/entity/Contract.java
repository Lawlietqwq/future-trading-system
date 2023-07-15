package com.csubigdata.futurestradingsystem.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class Contract {

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
