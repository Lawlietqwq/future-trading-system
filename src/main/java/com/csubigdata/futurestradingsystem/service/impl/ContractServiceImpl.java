package com.csubigdata.futurestradingsystem.service.impl;

import com.csubigdata.futurestradingsystem.common.CommonException;
import com.csubigdata.futurestradingsystem.common.ResultTypeEnum;
import com.csubigdata.futurestradingsystem.dao.ContractMapper;
import com.csubigdata.futurestradingsystem.dao.ModelMapper;
import com.csubigdata.futurestradingsystem.entity.Contract;
import com.csubigdata.futurestradingsystem.service.ContractService;
import com.csubigdata.futurestradingsystem.vo.ContractVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

@Service
@Scope("prototype")
public class ContractServiceImpl implements ContractService {

    @Autowired
    private ContractMapper contractMapper;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<String> getAllContractCode() {
        List<String> list = contractMapper.getAllContractCode();
        if(CollectionUtils.isEmpty(list)) CommonException.fail(ResultTypeEnum.QUERY_FAIL);
        return list;
    }

    @Override
    public List<Contract> getAllContract() {
        List<Contract> contractList = contractMapper.getAllContract();
        if (CollectionUtils.isEmpty(contractList)){
            CommonException.fail(ResultTypeEnum.QUERY_FAIL);
        }
        return contractList;
    }

    @Override
    public Contract getContractByCode(String code) {
        Contract contract = contractMapper.getContractByCode(code);
        if (contract == null){
            CommonException.fail(ResultTypeEnum.QUERY_FAIL);
        }
        return contract;
    }

    @Override
    public List<Contract> getLatestContractByCode(String code, int num) {
        List<Contract> latestData = contractMapper.getLatestByCode(code, num);
        if (CollectionUtils.isEmpty(latestData)){
            CommonException.fail(ResultTypeEnum.QUERY_FAIL);
        }
        return latestData;
    }
}
