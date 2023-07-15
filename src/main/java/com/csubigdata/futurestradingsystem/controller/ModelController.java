package com.csubigdata.futurestradingsystem.controller;

import com.csubigdata.futurestradingsystem.common.*;
import com.csubigdata.futurestradingsystem.entity.Model;
import com.csubigdata.futurestradingsystem.service.ModelService;
import com.csubigdata.futurestradingsystem.util.BeanUtil;
import com.csubigdata.futurestradingsystem.vo.NewCloseStrategyVO;
import com.csubigdata.futurestradingsystem.vo.TradingVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.csubigdata.futurestradingsystem.vo.ModelVO;
import java.util.List;

@RestController
@RequestMapping("/model")
public class ModelController {

    @Autowired
    private ModelService modelService;



    /**
     * 新建模型
     *
     * @param modelVO
     * @return
     */
    @RepeatSubmit
    @PostMapping("")
    public Result createModel(@RequestBody ModelVO modelVO) {
        Model model = new Model();
        model = (Model) BeanUtil.copyProperties(modelVO, model);
        modelService.createNewModel(model);
        return new Result<>(ResultTypeEnum.SUCCESS);
    }

    @RepeatSubmit
    @PostMapping("/batch")
    public Result createBatchModel(@RequestBody List<ModelVO> modelVOList) {
        for (ModelVO modelVO:modelVOList) {
            modelService.createNewModel(BeanUtil.copyProperties(modelVO, Model.class));
        }
        return new Result<>(ResultTypeEnum.SUCCESS);
    }


    /**
     * 根据模型Id获取模型
     *
     * @param modelId
     * @return
     */
    @GetMapping("/{modelId}")
    public Result<ModelVO> getModelById(@PathVariable("modelId") int modelId) {
        Result<ModelVO> result = new Result<>(ResultTypeEnum.SUCCESS);
        ModelVO modelVO = modelService.getModelById(modelId);
        result.setData(modelVO);
        return result;
    }

    /**
     * 管理员接口：得到所有模型
     *
     * @return
     */
    @GetMapping("")
    public Result<List<ModelVO>> getAllModel() {
        Result<List<ModelVO>> result = new Result<>(ResultTypeEnum.SUCCESS);
        List<ModelVO> list = modelService.getAll();
        result.setData(list);
        return result;
    }


    /**
     * 得到登录用户的所有模型
     *
     * @param uid
     * @return
     */
    @GetMapping("/user/{uid}")
    public Result<List<ModelVO>> getAllModelByUid(@PathVariable("uid") int uid) {
        Result<List<ModelVO>> result = new Result<>(ResultTypeEnum.SUCCESS);
        List<ModelVO> list = modelService.getUserAllModel(uid);
        result.setData(list);
        return result;
    }


    /**
     * 开启策略实例
     *
     * @param tradingVO
     * @return
     */
    @RepeatSubmit
    @PutMapping("/start")
    public Result startModel(@RequestBody TradingVO tradingVO) {
        modelService.startModel(tradingVO);
        return new Result<>(ResultTypeEnum.SUCCESS);
    }

    /**
     * 暂停策略实例
     *
     * @param modelId
     * @return
     */
    @RepeatSubmit
    @PutMapping("/pause")
    public Result pauseModel(@RequestParam("modelId") int modelId) {
        modelService.pauseModel(modelId);
        return new Result<>(ResultTypeEnum.SUCCESS);
    }

    /**
     * 强制平仓
     *
     * @param tradingVO
     * @return
     */
    @RepeatSubmit
    @PutMapping("/force")
    public Result forceClose(@RequestBody TradingVO tradingVO) {
        modelService.forceClose(tradingVO);
        return new Result<>(ResultTypeEnum.SUCCESS);
    }

    /**
     * 删除模型
     * @param modelId
     * @return
     */
    @RepeatSubmit
    @DeleteMapping("/{modelId}")
    public Result deleleModelById(@PathVariable("modelId") int modelId){
        modelService.deleteModel(modelId);
        return new Result<>(ResultTypeEnum.SUCCESS);
    }

    /**
     * 根据实例Id更新model
     * @param modelVO
     * @return
     */
    @PutMapping("")
    public Result updateModelById(@RequestBody ModelVO modelVO){
        Model model = new Model();
        model = (Model) BeanUtil.copyProperties(modelVO,model);
        modelService.updateModelById(model);
        return new Result<>(ResultTypeEnum.SUCCESS);

    }


    @PostMapping("/change")
    public Result changeCloseModel(@RequestBody NewCloseStrategyVO newCloseStrategyVO){
        modelService.createModelFromOld(newCloseStrategyVO);
        return new Result<>(ResultTypeEnum.SUCCESS);
    }

}
