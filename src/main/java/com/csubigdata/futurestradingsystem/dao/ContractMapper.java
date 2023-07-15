package com.csubigdata.futurestradingsystem.dao;

import com.csubigdata.futurestradingsystem.entity.Contract;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContractMapper {
    List<String> getAllContractCode();
    List<Contract> getAllContract();
    Contract getContractByCode(String code);
    List<Contract> getLatestByCode(@Param("code") String code, @Param("num") int num);

}
