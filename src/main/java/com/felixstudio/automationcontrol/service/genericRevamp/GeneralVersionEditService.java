package com.felixstudio.automationcontrol.service.genericRevamp;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.felixstudio.automationcontrol.dao.generic.GenericDAO;
import com.felixstudio.automationcontrol.dto.generic.GenericQueryDTO;
import com.felixstudio.automationcontrol.entity.genericRevamp.GeneralVersionEdit;

import java.util.List;

public interface GeneralVersionEditService extends IService<GeneralVersionEdit> {
    IPage<GenericDAO> queryByDto(GenericQueryDTO genericQueryDTO);

    Object updateOperationRemark(GeneralVersionEdit generalVersionEdit);

    Object saveBatchGenericsAndCreateTasks(List<GeneralVersionEdit> generalVersionEdits);

    Object deleteGeneric(Long id);
}
