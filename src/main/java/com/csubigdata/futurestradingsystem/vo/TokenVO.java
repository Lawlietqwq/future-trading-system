package com.csubigdata.futurestradingsystem.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenVO implements Serializable {

    private static final long serialVersionUID = 3706064430255280969L;

    //过期时间
    private Long expireTime;
    //token
    private String token;
}
