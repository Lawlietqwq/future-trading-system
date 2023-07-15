package com.csubigdata.futurestradingsystem.strategy.impl;

import com.csubigdata.futurestradingsystem.common.CommonException;
import com.csubigdata.futurestradingsystem.common.Constants;
import com.csubigdata.futurestradingsystem.common.ModelStateEnum;
import com.csubigdata.futurestradingsystem.common.ResultTypeEnum;
import com.csubigdata.futurestradingsystem.entity.Contract;
import com.csubigdata.futurestradingsystem.entity.Parameter;
import com.csubigdata.futurestradingsystem.service.ContractService;
import com.csubigdata.futurestradingsystem.service.ModelService;
import com.csubigdata.futurestradingsystem.service.PositionService;
import com.csubigdata.futurestradingsystem.strategy.AsyncTask;
import com.csubigdata.futurestradingsystem.strategy.ModelInstance;
import com.csubigdata.futurestradingsystem.strategy.TradingExecutable;
import com.csubigdata.futurestradingsystem.util.HttpClientUtil;
import com.csubigdata.futurestradingsystem.util.ModelUtil;
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
public class StopLoss implements TradingExecutable {

    private String code;
    private int uid;
    private int modelId;
    private String xinyiAccount;
    private String xinyiPwd;
    private String tradingAccount;
    private String tradingPwd;
    private String company;
    private int lot;
    private int activeProfit;
    private int passiveProfit;
    private boolean bkOrSk;
    private double bestPrice;
    private double openPrice;
    private ModelStateEnum modelState;
    private ModelService modelService;
    private ContractService contractService;
    private ReentrantLock lock;


    public StopLoss(String code, int uid, int modelId, String xinyiAccount, String xinyiPwd, String tradingAccount, String tradingPwd, String company, int lot, List<Parameter> closeParams, boolean bkOrSk, ModelStateEnum modelState, ReentrantLock lock){
        this.code = code;
        this.uid = uid;
        this.modelId = modelId;
        this.xinyiAccount = xinyiAccount;
        this.xinyiPwd = xinyiPwd;
        this.tradingAccount = tradingAccount;
        this.tradingPwd = tradingPwd;
        this.company = company;
        this.lot = lot;
        this.activeProfit = (int)closeParams.get(0).getParamValue();
        this.passiveProfit = (int)closeParams.get(1).getParamValue();
        this.bkOrSk = bkOrSk;
        this.modelState = modelState;
        this.lock = lock;
        this.modelService = SpringContextUtil.getBean(ModelService.class);
        this.contractService = SpringContextUtil.getBean(ContractService.class);
    }

    @Override
    public void trade(RedisContractVO contract){
        if (this.openPrice == 0.0){
            this.openPrice = ModelUtil.getOpenPrice(modelId);
            this.bestPrice = openPrice;
        }
        double nowPrice = contract.getClose();
        ModelInstance modelInstance = AsyncTask.getModelInstanceMap().get(modelId);
        if (bkOrSk){
            if(bestPrice > nowPrice) bestPrice = nowPrice;
            //SK——空头买平策略
            if (nowPrice - bestPrice >= bestPrice * passiveProfit / 100 || openPrice - nowPrice >= openPrice * activeProfit / 100) {
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
            if(bestPrice < nowPrice) bestPrice = nowPrice;
            //BK——多头卖平策略
            if (bestPrice - nowPrice >= bestPrice * passiveProfit / 100 || nowPrice - openPrice >= openPrice * activeProfit / 100) {
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
    }

    @Override
    public void clearData() {
    }
}
