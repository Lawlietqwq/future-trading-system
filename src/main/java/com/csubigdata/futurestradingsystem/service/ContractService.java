package com.csubigdata.futurestradingsystem.service;

import com.csubigdata.futurestradingsystem.entity.Contract;
import com.csubigdata.futurestradingsystem.vo.ContractVO;

import java.util.List;
import java.util.Map;


public interface ContractService {
    List<String> getAllContractCode();
    List<Contract> getAllContract();
    Contract getContractByCode(String code);
    List<Contract> getLatestContractByCode(String code, int num);
}
