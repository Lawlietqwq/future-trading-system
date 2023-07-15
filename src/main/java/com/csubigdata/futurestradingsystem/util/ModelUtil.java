package com.csubigdata.futurestradingsystem.util;

import com.csubigdata.futurestradingsystem.service.PositionService;
import com.csubigdata.futurestradingsystem.strategy.AsyncTask;
import com.csubigdata.futurestradingsystem.strategy.ModelInstance;
import org.springframework.stereotype.Component;

@Component
public class ModelUtil {
    public static double getOpenPrice(int modelId){
        return SpringContextUtil.getBean(PositionService.class).getAllHoldingByModelId(modelId).getOpenPrice();
    }

    public static void setLot(int modelId, int lot){
       ModelInstance modelInstance = AsyncTask.modelInstanceMap.get(modelId);
       modelInstance.setLot(lot);
       modelInstance.getOpenModel().setLot(lot);
       modelInstance.getCloseModel().setLot(lot);
    }
}
