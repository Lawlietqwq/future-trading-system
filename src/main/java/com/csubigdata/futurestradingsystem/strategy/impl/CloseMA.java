package com.csubigdata.futurestradingsystem.strategy.impl;

import com.csubigdata.futurestradingsystem.common.CommonException;
import com.csubigdata.futurestradingsystem.common.Constants;
import com.csubigdata.futurestradingsystem.common.ModelStateEnum;
import com.csubigdata.futurestradingsystem.common.ResultTypeEnum;
import com.csubigdata.futurestradingsystem.entity.Contract;
import com.csubigdata.futurestradingsystem.entity.Parameter;
import com.csubigdata.futurestradingsystem.service.ContractService;
import com.csubigdata.futurestradingsystem.service.ModelService;
import com.csubigdata.futurestradingsystem.strategy.AsyncTask;
import com.csubigdata.futurestradingsystem.strategy.ModelInstance;
import com.csubigdata.futurestradingsystem.strategy.TradingExecutable;
import com.csubigdata.futurestradingsystem.util.HttpClientUtil;
import com.csubigdata.futurestradingsystem.util.SpringContextUtil;
import com.csubigdata.futurestradingsystem.vo.RedisContractVO;
import com.csubigdata.futurestradingsystem.vo.RemoteVO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Data
@ToString
@NoArgsConstructor
public class CloseMA implements TradingExecutable {

    private String code;
    private int uid;
    private int modelId;
    private String xinyiAccount;
    private String xinyiPwd;
    private String tradingAccount;
    private String tradingPwd;
    private String company;
    private int lot;
    private int shortParam;
    private int longParam;
    private boolean bkOrSk;
    private ArrayList<Double> open = new ArrayList<>();
    private ArrayList<Double> close = new ArrayList<>();
    private List<Double> longMA = new ArrayList<>();
    private List<Double> shortMA = new ArrayList<>();
    private ModelStateEnum modelState;
    private ModelService modelService;
    private ContractService contractService;
    private ReentrantLock lock;


    public CloseMA(String code, int uid, int modelId, String xinyiAccount, String xinyiPwd, String tradingAccount, String tradingPwd, String company, int lot, List<Parameter> closeParams, boolean bkOrSk, ModelStateEnum modelState, ReentrantLock lock){
        this.code = code;
        this.uid = uid;
        this.modelId = modelId;
        this.xinyiAccount = xinyiAccount;
        this.xinyiPwd = xinyiPwd;
        this.tradingAccount = tradingAccount;
        this.tradingPwd = tradingPwd;
        this.company = company;
        this.lot = lot;
        this.shortParam = (int)closeParams.get(0).getParamValue();
        this.longParam = (int)closeParams.get(1).getParamValue();
        this.bkOrSk = bkOrSk;
        this.modelState = modelState;
        this.lock = lock;
        this.modelService = SpringContextUtil.getBean(ModelService.class);
        this.contractService = SpringContextUtil.getBean(ContractService.class);
    }

    public void initial(){
        List<Contract> contractList = contractService.getLatestContractByCode(code, longParam + 1);
        double sum1 = 0;
        double sum2 = 0;
        double sum3 = 0;
        double sum4 = 0;
        double tmpOpen = 0;
        double tmpClose = 0;
        for (int i = contractList.size() - 1; i >=0; i--){
            tmpOpen = contractList.get(i).getOpen();
            tmpClose = contractList.get(i).getClose();
            if (i >= contractList.size() - shortParam - 1 && i != contractList.size() - 1){
                sum1 += tmpOpen;
            }
            if (i >= contractList.size() - shortParam){
                sum2 += tmpOpen;
            }
            if (i >= contractList.size() - longParam - 1 && i != contractList.size() - 1){
                sum3 += tmpOpen;
            }
            if (i >= contractList.size() - longParam){
                sum4 += tmpOpen;
            }

            open.add(tmpOpen);
            close.add(tmpClose);
        }
        shortMA.add(sum1/shortParam);
        shortMA.add(sum2/shortParam);
        longMA.add(sum3/longParam);
        longMA.add(sum4/longParam);
    }

    @Override
    public void trade(RedisContractVO contract){
        if (longMA.size() == 0){
            initial();
            open.add(contract.getOpen());
            close.add(contract.getClose());
        }
        if (contract.isMin_started()){
            open.add(contract.getOpen());
            close.add(contract.getClose());
        }else {
            int size = open.size() - 1;
            open.set(size, contract.getOpen());
            close.set(size, contract.getClose());
        }
        //计算慢均线
        longMA.add(open.subList(open.size()-longParam, open.size()).stream().mapToDouble(Double::doubleValue).sum()/longParam);
        //计算快均线
        shortMA.add(open.subList(open.size()-shortParam, open.size()).stream().mapToDouble(Double::doubleValue).sum()/shortParam);
        int longSize = longMA.size();
        int shortSize = shortMA.size();
        ModelInstance modelInstance = AsyncTask.getModelInstanceMap().get(modelId);
        if (bkOrSk){
            //SK——空头买平策略
            if (longMA.get(longSize-2) <= shortMA.get(shortSize-2) && longMA.get(longSize-1) >= shortMA.get(shortSize-1)) {
                //交易平仓
                if (!modelInstance.isFinished() && lock.tryLock()) {
                    modelService.updateModelStateById(modelId, ModelStateEnum.closing);
                    modelInstance.setModelStatus(ModelStateEnum.closing);
                    RemoteVO remoteVO = new RemoteVO(uid, modelId, code, xinyiAccount, xinyiPwd, tradingAccount, tradingPwd, company, bkOrSk, lot);
                    int code = HttpClientUtil.doPostParams(remoteVO, Constants.CLOSE_URL);
                    if (code == 200) {
                        modelService.successClose(modelId);
                        lock.unlock();
                    }  else {
                        modelService.updateModelStateById(modelId, ModelStateEnum.holding);
                        modelInstance.setModelStatus(ModelStateEnum.holding);
                        lock.unlock();
                        if (code == 500) CommonException.fail(ResultTypeEnum.CLOSE_ERROR);
                    }
                }
            }
        } else {
            //BK——多头卖平策略
            if (longMA.get(longSize-2) >= shortMA.get(shortSize-2) && longMA.get(longSize-1) <= shortMA.get(shortSize-1)){
                //交易平仓
                if(!modelInstance.isFinished() && lock.tryLock()) {
                    modelService.updateModelStateById(modelId, ModelStateEnum.closing);
                    modelInstance.setModelStatus(ModelStateEnum.closing);
                    RemoteVO remoteVO = new RemoteVO(uid, modelId, code, xinyiAccount, xinyiPwd, tradingAccount, tradingPwd, company, bkOrSk, lot);
                    int code = HttpClientUtil.doPostParams(remoteVO, Constants.CLOSE_URL);
                    if (code == 200) {
                        modelService.successClose(modelId);
                        lock.unlock();
                    } else {
                        modelService.updateModelStateById(modelId, ModelStateEnum.holding);
                        modelInstance.setModelStatus(ModelStateEnum.holding);
                        lock.unlock();
                        if (code == 500) CommonException.fail(ResultTypeEnum.CLOSE_ERROR);
                    }
                }
            }
        }
        clearData();
    }

    @Override
    public void clearData() {
        if (this.longMA.size()>=3){
            int size = this.open.size();
            this.open.subList(0, size - longParam).clear();
            this.close.subList(0, size - longParam).clear();
        }
    }
}
