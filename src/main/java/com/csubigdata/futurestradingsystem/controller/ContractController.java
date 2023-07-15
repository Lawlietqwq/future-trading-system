package com.csubigdata.futurestradingsystem.controller;

import com.csubigdata.futurestradingsystem.common.Result;
import com.csubigdata.futurestradingsystem.common.ResultTypeEnum;
import com.csubigdata.futurestradingsystem.entity.Contract;
import com.csubigdata.futurestradingsystem.entity.User;
import com.csubigdata.futurestradingsystem.service.ContractService;
import com.csubigdata.futurestradingsystem.vo.ContractVO;
import com.csubigdata.futurestradingsystem.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/contract")
public class ContractController {

    @Autowired
    private ContractService contractService;

    @GetMapping("/code")
    public Result<List<String>> getAllContractCode(){
        List<String> list = contractService.getAllContractCode();
        Result<List<String>> result=new Result<>(ResultTypeEnum.SUCCESS);
        result.setData(list);
        return result;
    }

    @GetMapping("")
    public Result<List<Contract>> getAllContract(){
        List<Contract> contractList = contractService.getAllContract();
        Result<List<Contract>> result=new Result<>(ResultTypeEnum.SUCCESS);
        result.setData(contractList);
        return result;
    }
}
