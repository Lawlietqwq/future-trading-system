package com.csubigdata.futurestradingsystem.strategy;

import com.csubigdata.futurestradingsystem.common.Constants;
import com.csubigdata.futurestradingsystem.config.redis.RedisService;
import com.csubigdata.futurestradingsystem.service.ContractService;
import com.csubigdata.futurestradingsystem.service.ModelService;
import com.csubigdata.futurestradingsystem.util.JsonUtil;
import com.csubigdata.futurestradingsystem.util.SpringContextUtil;
import com.csubigdata.futurestradingsystem.vo.RedisContractVO;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

@Component("asyncTask")
@EnableAspectJAutoProxy(exposeProxy = true,proxyTargetClass = true)
@EnableScheduling
@EnableAsync
@Data
@Slf4j
public class AsyncTask {

    public static Map<Integer,ModelInstance> modelInstanceMap = new HashMap<>();
    @Resource
    private ContractService contractService;
    @Resource
    private ModelService modelService;
    @Resource
    private RedisService redisService;

    public void setModelInstance(ModelInstance modelInstance){
        modelInstanceMap.put(modelInstance.getModelId() , modelInstance);
//        if (!CollectionUtils.isEmpty(modelInstanceMap)) run(modelInstance);
    }

    @Bean("modelInstanceMap")
    public static Map<Integer,ModelInstance> getModelInstanceMap(){
        if (modelInstanceMap == null){
            modelInstanceMap = new HashMap<>();
        }
        return modelInstanceMap;
    }

    public static AsyncTask getAsyncTask(){
        return (AsyncTask)SpringContextUtil.getBean("asyncTask");
    }

    @Scheduled(cron = "* * 0-2,9-14,21-23 ? * MON-FRI")
    @Transactional
    public void runAll(){
        synchronized (this) {
            if (!CollectionUtils.isEmpty(modelInstanceMap)) {
                AsyncTask asyncTask = ((AsyncTask)AopContext.currentProxy());
                Iterator<Integer> iterator = modelInstanceMap.keySet().iterator();
                while (iterator.hasNext()) {
                    Integer key = iterator.next();
                    if (modelInstanceMap.get(key).isFinished()) {
                        ReentrantLock lock = modelInstanceMap.get(key).getLock();
                        if (lock.tryLock()) {
                            iterator.remove();
                            lock.unlock();
                        }
                    } else {
                        asyncTask.run(modelInstanceMap.get(key));
                    }
                }
            }
        }
    }

    @Async("taskExecutor")
    public void run(ModelInstance modelInstance) {
        {
            String contractStr = redisService.get(modelInstance.getCode());
            RedisContractVO contract = JsonUtil.jsonToObj(contractStr, RedisContractVO.class);
            // 不是最新数据直接跳过
//            if (contract == null || new Date().getTime() - contract.getTrade_date().getTime() >= 1)
//                return;
            if (contract == null)
                return;
            if (modelInstance.getModelState().getState().equals("started")){
                modelInstance.getOpenModel().trade(contract);
            } else if (modelInstance.getModelState().getState().equals("holding") && !modelInstance.getCloseModel().getClass().getName().equals(Constants.MODEL_PREFIX + Constants.CLOSE_DEFAULT)){
                modelInstance.getCloseModel().trade(contract);
            }
        }
    }
}
