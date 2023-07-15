package com.csubigdata.futurestradingsystem.strategy;

import com.csubigdata.futurestradingsystem.common.CommonException;
import com.csubigdata.futurestradingsystem.common.Constants;
import com.csubigdata.futurestradingsystem.common.ModelStateEnum;
import com.csubigdata.futurestradingsystem.common.ResultTypeEnum;
import com.csubigdata.futurestradingsystem.entity.Parameter;
import lombok.Data;
import lombok.ToString;


import java.util.List;
import java.util.concurrent.locks.ReentrantLock;


@Data
@ToString
public class ModelInstance <Open extends TradingExecutable,Close extends TradingExecutable> {


    private String code;
    private int uid;
    private int modelId;
    private String xinyiAccount;
    private String xinyiPwd;
    private String tradingAccount;
    private String tradingPwd;
    private String company;
    private List<Parameter> openParams;
    private List<Parameter> closeParams;
    private boolean bkOrSk;
    private Open openModel;
    private Close closeModel;
    private int lot;
    private ModelStateEnum modelState = ModelStateEnum.started;
//    private double profit;
    private ReentrantLock lock;
    private boolean finished = false;


    public ModelInstance(String code, int uid, int modelId, String xinyiAccount, String xinyiPwd, String tradingAccount, String tradingPwd, String company, Class<Open> openClazz, Class<Close> closeClazz, List<Parameter> openParams,List<Parameter> closeParams, boolean bkOrSk, int lot, ModelStateEnum modelState){
        this.code = code;
        this.uid = uid;
        this.modelId = modelId;
        this.xinyiAccount = xinyiAccount;
        this.xinyiPwd = xinyiPwd;
        this.tradingAccount = tradingAccount;
        this.tradingPwd = tradingPwd;
        this.company = company;
        this.openParams = openParams;
        this.closeParams = closeParams;
        this.bkOrSk = bkOrSk;
        this.lot = lot;
        this.lock = new ReentrantLock();
        this.modelState = modelState;
        try {
            openModel = openClazz.getDeclaredConstructor(
                    String.class,
                    int.class,
                    int.class,
                    String.class,
                    String.class,
                    String.class,
                    String.class,
                    String.class,
                    int.class,
                    List.class,
                    boolean.class,
                    ModelStateEnum.class,
                    ReentrantLock.class
            ).newInstance(code, uid, modelId, xinyiAccount, xinyiPwd, tradingAccount, tradingPwd, company, lot, openParams, bkOrSk, modelState, lock);
            if (closeClazz.getName().equals(Constants.MODEL_PREFIX + Constants.CLOSE_DEFAULT)){
                closeModel = closeClazz.getDeclaredConstructor().newInstance();
            }else {
                closeModel = closeClazz.getDeclaredConstructor(
                        String.class,
                        int.class,
                        int.class,
                        String.class,
                        String.class,
                        String.class,
                        String.class,
                        String.class,
                        int.class,
                        List.class,
                        boolean.class,
                        ModelStateEnum.class,
                        ReentrantLock.class
                ).newInstance(code, uid, modelId, xinyiAccount, xinyiPwd, tradingAccount, tradingPwd, company, lot, closeParams, bkOrSk, modelState, lock);
            }
        } catch (Exception e){
            CommonException.fail(ResultTypeEnum.ERROR);
        }
    }

    public void setModelStatus(ModelStateEnum modelState){
        this.modelState = modelState;
        this.openModel.setModelState(modelState);
        this.closeModel.setModelState(modelState);
    }

}
