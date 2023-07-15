package com.csubigdata.futurestradingsystem.dao;

import com.csubigdata.futurestradingsystem.common.ModelStateEnum;
import com.csubigdata.futurestradingsystem.dto.ModelInstanceDTO;
import com.csubigdata.futurestradingsystem.entity.Model;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ModelMapper {
    List<Model> getAll();
    List<Model> getAllModelByUid(Integer uid);

    Model getById(Integer modelId);

    List<ModelInstanceDTO> getAllToMemory();

    int insert(Model model);

    int insertSelective(Model model);

    int updateStateById(@Param("modelId") int modelId, @Param("modelState")ModelStateEnum modelState, @Param("startTime") Date startTime, @Param("endTime") Date endTime);

    int updateByIdSelective(Model model);

    int deleteById(Integer modelId);


}
