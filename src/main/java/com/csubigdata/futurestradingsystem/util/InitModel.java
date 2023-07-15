package com.csubigdata.futurestradingsystem.util;

import com.csubigdata.futurestradingsystem.common.Constants;
import com.csubigdata.futurestradingsystem.dao.ModelMapper;
import com.csubigdata.futurestradingsystem.dto.ModelInstanceDTO;
import com.csubigdata.futurestradingsystem.strategy.AsyncTask;
import com.csubigdata.futurestradingsystem.strategy.ModelInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Component // 注意 这里必须有
@DependsOn({"springContextUtil", "jsonUtil", "modelUtil", "asyncTask", "springThreadPoolConfig", "taskExecutor"})
public class InitModel {

        @Autowired
        AsyncTask asyncTask;

        @Autowired
        ModelMapper modelMapper;

        @PostConstruct // 构造函数之后执行
        public void init(){
            startJob();
        }

        public void startJob(){
            try {
                List<ModelInstanceDTO> modelInstanceDTOS = modelMapper.getAllToMemory();
                if (CollectionUtils.isEmpty(modelInstanceDTOS)) return;
                for (ModelInstanceDTO modelInstanceDTO : modelInstanceDTOS) {
                    int lot = modelInstanceDTO.getLot();
                    if (modelInstanceDTO.getModelState().getState().equals("started")){
                        lot = modelMapper.getById(modelInstanceDTO.getModelId()).getLot();
                    }
                    asyncTask.setModelInstance(new ModelInstance(
                            modelInstanceDTO.getCode(),
                            modelInstanceDTO.getUid(),
                            modelInstanceDTO.getModelId(),
                            modelInstanceDTO.getXinyiAccount(),
                            modelInstanceDTO.getXinyiPwd(),
                            modelInstanceDTO.getTradingAccount(),
                            modelInstanceDTO.getTradingPwd(),
                            modelInstanceDTO.getCompany(),
                            Class.forName(Constants.MODEL_PREFIX + modelInstanceDTO.getOpenClass()),
                            Class.forName(Constants.MODEL_PREFIX + modelInstanceDTO.getCloseClass()),
                            modelInstanceDTO.getOpenParams(),
                            modelInstanceDTO.getCloseParams(),
                            modelInstanceDTO.isBkOrSk(),
                            lot,
                            modelInstanceDTO.getModelState()
                    ));
                }
                asyncTask.runAll();
            } catch (ClassNotFoundException e) {
                log.error("实例重新加载错误");
            }
        }
}
