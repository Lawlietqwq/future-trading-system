package com.csubigdata.futurestradingsystem.service;

import com.csubigdata.futurestradingsystem.entity.Contract;
import com.csubigdata.futurestradingsystem.vo.ContractVO;

import java.util.List;
import java.util.Map;


public interface ContractService {
    /**
     * 获取所有的期货合约代码
     * @return 合约代码的列表
     */
    List<String> getAllContractCode();

    /**
     * 获取所有合约的所有数据
     * @return 合约数据的列表
     */
    List<Contract> getAllContract();


    Contract getContractByCode(String code);

    /**
     * 根据合约代码取出最新的num条数据
     * @param code 合约代码
     * @param num 数量
     * @return
     */
    List<Contract> getLatestContractByCode(String code, int num);
}
