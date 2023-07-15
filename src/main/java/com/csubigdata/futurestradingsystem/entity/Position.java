package com.csubigdata.futurestradingsystem.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class Position {

    private int holdingId;

    private int uid;

    private int modelId;

    private String code;

    private double openPrice;

    private int openNum;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date openTime;

    private boolean bkOrSk;

}
