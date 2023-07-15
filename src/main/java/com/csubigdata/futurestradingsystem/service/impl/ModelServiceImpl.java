package com.csubigdata.futurestradingsystem.service.impl;

import com.csubigdata.futurestradingsystem.common.CommonException;
import com.csubigdata.futurestradingsystem.common.Constants;
import com.csubigdata.futurestradingsystem.common.ModelStateEnum;
import com.csubigdata.futurestradingsystem.common.ResultTypeEnum;
import com.csubigdata.futurestradingsystem.dao.ContractMapper;
import com.csubigdata.futurestradingsystem.dao.ModelMapper;
import com.csubigdata.futurestradingsystem.dao.ParameterMapper;
import com.csubigdata.futurestradingsystem.dao.PositionMapper;
import com.csubigdata.futurestradingsystem.entity.Model;
import com.csubigdata.futurestradingsystem.entity.Parameter;
import com.csubigdata.futurestradingsystem.entity.Position;
import com.csubigdata.futurestradingsystem.service.HttpClientService;
import com.csubigdata.futurestradingsystem.service.ModelService;
import com.csubigdata.futurestradingsystem.strategy.AsyncTask;
import com.csubigdata.futurestradingsystem.strategy.ModelInstance;
import com.csubigdata.futurestradingsystem.util.BeanUtil;
import com.csubigdata.futurestradingsystem.util.ModelUtil;
import com.csubigdata.futurestradingsystem.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Slf4j
@EnableAspectJAutoProxy(exposeProxy = true,proxyTargetClass = true)
public class ModelServiceImpl implements ModelService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ParameterMapper parameterMapper;

    @Autowired
    private ContractMapper contractMapper;

    @Autowired
    private PositionMapper positionMapper;

    @Autowired
    private HttpClientService httpClientService;



    @Override
    public List<ModelVO> getAll() {
        List<Model> models = modelMapper.getAll();
        List<ModelVO> modelVOList;
        if (!CollectionUtils.isEmpty(models)) {
            modelVOList = BeanUtil.copyList(models, ModelVO.class);
        }
        else {
            modelVOList=null;
        }
        return modelVOList;
    }

    @Override
    public List<ModelVO> getUserAllModel(int uid) {
        List<Model> models = modelMapper.getAllModelByUid(uid);
        List<ModelVO> modelVOS;

        if (!CollectionUtils.isEmpty(models)) {
            modelVOS = BeanUtil.copyList(models, ModelVO.class);
        }
        else return null;
        return modelVOS;
    }

    @Override
    public ModelVO getModelById(int modelId) {
        Model model = modelMapper.getById(modelId);
        ModelVO modelVO = new ModelVO();
        if (model == null) {
            CommonException.fail(ResultTypeEnum.QUERY_FAIL);
        } else {
            modelVO = (ModelVO)BeanUtil.copyProperties(model, modelVO);
        }
        return modelVO;
    }

    @Transactional(rollbackFor = CommonException.class)
    @Override
    public void createNewModel(Model model) {
        model.setCreateTime(new Date());
        if (model.getModelState() == null) model.setModelState(ModelStateEnum.created);
        boolean success = modelMapper.insertSelective(model) > 0;
        if(!success){
            CommonException.fail(ResultTypeEnum.CREATE_MODEL_ERROR);
        }
        if(model.getOpenParams()!=null){
            int modelId = model.getModelId();
            for(Parameter parameter :model.getOpenParams()){
                parameter.setModelId(modelId);
                boolean insertParamsSuccess = parameterMapper.insertParams(parameter);
                if(!insertParamsSuccess){
                    CommonException.fail(ResultTypeEnum.CREATE_PARAM_ERROR);
                }
            }
        }
        if(model.getCloseParams()!=null){
            int modelId = model.getModelId();
            for(Parameter parameter :model.getCloseParams()){
                parameter.setModelId(modelId);
                boolean insertParamsSuccess = parameterMapper.insertParams(parameter);
                if(!insertParamsSuccess){
                    CommonException.fail(ResultTypeEnum.CREATE_PARAM_ERROR);
                }
            }
        }
    }

    @Override
    @Transactional(rollbackFor = CommonException.class)
    public void updateModelById(Model model) {
        boolean success = modelMapper.updateByIdSelective(model) > 0;
        if(!success){
            CommonException.fail(ResultTypeEnum.UPDATE_MODEL_ERROR);
        }
        List<Parameter> openList = model.getOpenParams();
        if(openList != null) {
            for (Parameter param : model.getOpenParams())
                parameterMapper.updateParamById(param);
        }
        List<Parameter> closeList = model.getCloseParams();
        if(closeList != null) {
            for (Parameter param : model.getOpenParams())
                parameterMapper.updateParamById(param);
        }
    }

    @Override
    public void updateModelStateById(int modelId, ModelStateEnum modelState){
        String state = modelState.getState();
        Date startTime = null;
        Date endTime = null;
        if(state.equals("closed") || state.equals("created")) {
            endTime = new Date();
        }
        if (state.equals("started")){
            startTime = new Date();
        }

        boolean success = modelMapper.updateStateById(modelId, modelState, startTime, endTime) > 0;
        if(!success) CommonException.fail(ResultTypeEnum.UPDATE_MODEL_ERROR);
    }

    @Override
    @Transactional
    public void createModelFromOld(NewCloseStrategyVO newCloseStrategyVO) {
        Map<Integer, ModelInstance> modelInstanceMap = AsyncTask.modelInstanceMap;
        int oldModelId = newCloseStrategyVO.getModelId();
        Class openClazz = null;
        Class closeClazz = null;
        ModelVO oldModel = ((ModelService) AopContext.currentProxy()).getModelById(newCloseStrategyVO.getModelId());
        Model newModel = BeanUtil.copyProperties(oldModel, Model.class);
        newModel.setCloseId(newCloseStrategyVO.getCloseId());
        newModel.setCloseName(newCloseStrategyVO.getCloseName());
        newModel.setCloseClass(newCloseStrategyVO.getCloseClass());
        newModel.setCloseParams(newCloseStrategyVO.getCloseParams());
        if (!modelInstanceMap.containsKey(oldModelId)) {
            ((ModelService) AopContext.currentProxy()).deleteModel(oldModelId);
            newModel.setLot(newCloseStrategyVO.getLot());
            ((ModelService) AopContext.currentProxy()).createNewModel(newModel);
            return;
        }else {
            boolean isFinished = modelInstanceMap.get(oldModelId).isFinished();
            if (isFinished){
                ReentrantLock lock = modelInstanceMap.get(oldModelId).getLock();
                if (lock.tryLock()){
                    ((ModelService) AopContext.currentProxy()).deleteModel(oldModelId);
                    newModel.setLot(newCloseStrategyVO.getLot());
                    ((ModelService) AopContext.currentProxy()).createNewModel(newModel);
                    lock.unlock();
                    return;
                }
            }
        }
        ModelInstance modelInstance = AsyncTask.getModelInstanceMap().get(oldModelId);
        ReentrantLock lock = modelInstance.getLock();
        if (!modelInstance.isFinished() && lock.tryLock()) {
            int lot = modelInstance.getModelState().getState().equals("started")? newCloseStrategyVO.getLot():Math.min(newCloseStrategyVO.getLot(), modelInstance.getLot());
            if (modelInstance.getModelState().getState().equals("started")){
                ((ModelService) AopContext.currentProxy()).deleteModel(oldModelId);
                newModel.setLot(lot);
                ((ModelService) AopContext.currentProxy()).createNewModel(newModel);
                modelInstance.setFinished(true);
                lock.unlock();
            }else {
                try {
                    openClazz = modelInstance.getOpenModel().getClass();
                    closeClazz = Class.forName(Constants.MODEL_PREFIX + newCloseStrategyVO.getCloseClass());
                } catch (ClassNotFoundException e) {
                    CommonException.fail(ResultTypeEnum.CLASS_NOT_FOUND);
                }
                //新建model
                newModel.setLot(lot);
                ((ModelService) AopContext.currentProxy()).createNewModel(newModel);
                int newModelId = newModel.getModelId();
                int remainLot = modelInstance.getLot() - lot;
                Position newPosition = null;
                PositionVO oldPosition = positionMapper.getAllHoldingByModelId(modelInstance.getModelId());
                newPosition = new Position(0, oldPosition.getUid(), newModelId, oldPosition.getCode(), oldPosition.getOpenPrice(), lot, oldPosition.getOpenTime(), oldPosition.isBkOrSk());

                if (remainLot == 0) {
//                modelInstanceMap.keySet().removeIf(key -> key == modelInstance.getModelId());
                    modelInstance.setFinished(true);
//                ((ModelService) AopContext.currentProxy()).updateModelStateById(modelInstance.getModelId(), ModelStateEnum.created);
                    boolean succuss = modelMapper.deleteById(modelInstance.getModelId()) > 0;
                    if (!succuss) CommonException.fail(ResultTypeEnum.DELETE_MODEL_ERROR);
                    positionMapper.updateLotById(modelInstance.getModelId(), newModelId, lot);
                } else {
                    Model model = BeanUtil.copyProperties(oldModel, Model.class);
                    model.setLot(remainLot);
                    ((ModelService) AopContext.currentProxy()).updateModelById(model);
                    positionMapper.insert(newPosition);
                    positionMapper.updateLotById(modelInstance.getModelId(), null, remainLot);
                }
                ModelUtil.setLot(oldModelId, remainLot);
            }
            AsyncTask.getAsyncTask().setModelInstance(new ModelInstance(
                    modelInstance.getCode(),
                    modelInstance.getUid(),
                    newModel.getModelId(),
                    modelInstance.getXinyiAccount(),
                    modelInstance.getXinyiPwd(),
                    modelInstance.getTradingAccount(),
                    modelInstance.getTradingPwd(),
                    modelInstance.getCompany(),
                    openClazz,
                    closeClazz,
                    modelInstance.getOpenParams(),
                    newCloseStrategyVO.getCloseParams(),
                    modelInstance.isBkOrSk(),
                    lot,
                    modelInstance.getModelState()
            ));
            lock.unlock();
        } else {
            if (modelInstance.getModelState().getState().equals("started")) CommonException.fail(ResultTypeEnum.IS_OPENNING);
            else CommonException.fail(ResultTypeEnum.IS_CLOSING);
        }
    }
    @Override
    public void deleteModel(int modelId) {
        if (!AsyncTask.getModelInstanceMap().containsKey(modelId) || AsyncTask.getModelInstanceMap().get(modelId).isFinished()) {
            boolean success = modelMapper.deleteById(modelId) > 0;
            if (!success) {
                CommonException.fail(ResultTypeEnum.DELETE_MODEL_ERROR);
            }
        }
        else {
            CommonException.fail(ResultTypeEnum.DELETE_MODEL_ERROR);
        }
//            ReentrantLock lock = AsyncTask.getModelInstanceMap().get(modelId).getLock();
//            if (lock.tryLock()) {
//                boolean success = modelMapper.deleteById(modelId) > 0;
//                lock.unlock();
//                if (!success) CommonException.fail(ResultTypeEnum.DELETE_MODEL_ERROR);
//            } else CommonException.fail(ResultTypeEnum.DELETE_CONFLICT);
//        }
    }

    @Override
    @Transactional
    public void startModel(TradingVO tradingVO) {
        Model model = modelMapper.getById(tradingVO.getModelId());
        if (!model.getModelState().getState().equals("created") && !model.getModelState().getState().equals("closed")){
            CommonException.fail(ResultTypeEnum.START_ERROR);
        }
        else {
            if (AsyncTask.getModelInstanceMap().containsKey(model.getModelId())) {
                ModelInstance oldModelInstance = AsyncTask.getModelInstanceMap().get(model.getModelId());
                ReentrantLock oldLock = oldModelInstance.getLock();
                if (oldModelInstance.isFinished() && oldLock.tryLock()) {
                    try {
                        ModelInstance modelInstance = new ModelInstance(
                                model.getCode(),
                                model.getUid(),
                                model.getModelId(),
                                tradingVO.getXinyiAccount(),
                                tradingVO.getXinyiPwd(),
                                tradingVO.getTradingAccount(),
                                tradingVO.getTradingPwd(),
                                tradingVO.getCompany(),
                                Class.forName(Constants.MODEL_PREFIX + model.getOpenClass()),
                                Class.forName(Constants.MODEL_PREFIX + model.getCloseClass()),
                                model.getOpenParams(),
                                model.getCloseParams(),
                                model.isBkOrSk(),
                                model.getLot(),
                                ModelStateEnum.started
                        );
                        ReentrantLock lock = modelInstance.getLock();
                        lock.lock();
                        try {
                            ((ModelService) AopContext.currentProxy()).updateModelStateById(model.getModelId(), ModelStateEnum.started);
                            AsyncTask.getAsyncTask().setModelInstance(modelInstance);
                        } catch (CommonException e) {
                            throw e;
                        } finally {
                            lock.unlock();
                        }
                    } catch (ClassNotFoundException e) {
                        CommonException.fail(ResultTypeEnum.START_ERROR);
                    }
                    oldLock.unlock();
                }
            } else {
                try {
                    ModelInstance modelInstance = new ModelInstance(
                            model.getCode(),
                            model.getUid(),
                            model.getModelId(),
                            tradingVO.getXinyiAccount(),
                            tradingVO.getXinyiPwd(),
                            tradingVO.getTradingAccount(),
                            tradingVO.getTradingPwd(),
                            tradingVO.getCompany(),
                            Class.forName(Constants.MODEL_PREFIX + model.getOpenClass()),
                            Class.forName(Constants.MODEL_PREFIX + model.getCloseClass()),
                            model.getOpenParams(),
                            model.getCloseParams(),
                            model.isBkOrSk(),
                            model.getLot(),
                            ModelStateEnum.started
                    );
                    ReentrantLock lock = modelInstance.getLock();
                    lock.lock();
                    try {
                        ((ModelService) AopContext.currentProxy()).updateModelStateById(model.getModelId(), ModelStateEnum.started);
                        AsyncTask.getAsyncTask().setModelInstance(modelInstance);
                    } catch (CommonException e) {
                        throw e;
                    } finally {
                        lock.unlock();
                    }
                } catch (ClassNotFoundException e) {
                    CommonException.fail(ResultTypeEnum.START_ERROR);
                }
            }
        }
    }

    @Override
    @Transactional
    public void pauseModel(int modelId) {
            if (!AsyncTask.getModelInstanceMap().containsKey(modelId) || AsyncTask.getModelInstanceMap().get(modelId).isFinished()){
                CommonException.fail(ResultTypeEnum.NO_START_ERROR);
            }
            ReentrantLock lock = AsyncTask.getModelInstanceMap().get(modelId).getLock();
            if (lock.tryLock()){
                ModelStateEnum modelState = modelMapper.getById(modelId).getModelState();
                if (modelState.getState().equals("started")){
//                    AsyncTask.modelInstanceMap.keySet().removeIf(key -> key == modelId);
                    AsyncTask.modelInstanceMap.get(modelId).setFinished(true);
                    ((ModelService)AopContext.currentProxy()).updateModelStateById(modelId, ModelStateEnum.created);
                    lock.unlock();
                }
            } else CommonException.fail(ResultTypeEnum.PAUSE_ERROR);
    }

    @Override
    @Transactional
    public void open(int modelId) {
        ((ModelService)AopContext.currentProxy()).updateModelStateById(modelId, ModelStateEnum.holding);
    }

    @Override
    @Transactional
    public void forceClose(TradingVO tradingVO) {
        int modelId = tradingVO.getModelId();
        if (!AsyncTask.getModelInstanceMap().containsKey(modelId) || AsyncTask.getModelInstanceMap().get(modelId).isFinished()) {
            CommonException.fail(ResultTypeEnum.FORCE_CLOSE_ERROR);
        } else {
            ReentrantLock lock = AsyncTask.getModelInstanceMap().get(modelId).getLock();
            if (lock.tryLock()) {
                Model model = modelMapper.getById(modelId);
                if (model.getModelState().getState().equals("holding")) {
                    ((ModelService)AopContext.currentProxy()).updateModelStateById(modelId, ModelStateEnum.closing);
//                    double price = contractMapper.getContractByCode(model.getCode()).getClose();
                    int lot = tradingVO.getLot();
                    int remainLot = positionMapper.getOpenNumByModelId(model.getModelId());
                    lot = Math.min(lot, remainLot);
                    RemoteVO remoteVO = new RemoteVO(
                            model.getUid(),
                            model.getModelId(),
                            model.getCode(),
                            tradingVO.getXinyiAccount(),
                            tradingVO.getXinyiPwd(),
                            tradingVO.getTradingAccount(),
                            tradingVO.getTradingPwd(),
                            tradingVO.getCompany(),
                            model.isBkOrSk(),
                            lot
                    );
                    int code = httpClientService.doPostParams(remoteVO, Constants.CLOSE_URL);
                    if (code == 200) {
//                        AsyncTask.modelInstanceMap.keySet().removeIf(key -> key == modelId);
                        AsyncTask.modelInstanceMap.get(modelId).setFinished(true);
                        ((ModelService)AopContext.currentProxy()).updateModelStateById(modelId, ModelStateEnum.closed);
                        ModelUtil.setLot(modelId, remainLot - lot);
                        lock.unlock();
                    } else {
                        ((ModelService)AopContext.currentProxy()).updateModelStateById(modelId, ModelStateEnum.holding);
                        ModelUtil.setLot(modelId, remainLot - lot);
                        lock.unlock();
                        if (code == 500) CommonException.fail(ResultTypeEnum.FORCE_CLOSE_ERROR);
                    }
                } else {
                    lock.unlock();
                    CommonException.fail(ResultTypeEnum.NO_HOLDING_ERROR);
                }
            }  else CommonException.fail(ResultTypeEnum.FORCE_CLOSE_ERROR);
        }
    }

    @Override
    @Transactional
    public void successClose(int modelId) {
        Model model = new Model();
        model.setModelId(modelId);
        model.setModelState(ModelStateEnum.closed);
        model.setEndTime(new Date());
//        AsyncTask.modelInstanceMap.keySet().removeIf(key -> key == modelId);
        boolean success = modelMapper.updateByIdSelective(model) > 0;
        AsyncTask.modelInstanceMap.get(modelId).setFinished(true);
        if (!success){
            CommonException.fail(ResultTypeEnum.CLOSE_ERROR);
        }
    }
}
