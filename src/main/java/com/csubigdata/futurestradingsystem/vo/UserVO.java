package com.csubigdata.futurestradingsystem.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVO implements Serializable {

    private static final long serialVersionUID = -1346605042549459936L;

    private Long uid;

    private String username;

    private String email;

    private String xinyiAccount;

    private String xinyiPwd;

    private String tradingAccount;

    private String tradingPwd;

    private String company;

}
