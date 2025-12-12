package com.felixstudio.automationcontrol.service.changeCode;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.felixstudio.automationcontrol.dto.changeCode.ChangeCodeDTO;
import com.felixstudio.automationcontrol.dto.changeCode.ChangeCodeQueryDTO;
import com.felixstudio.automationcontrol.entity.changeCode.ChangeCode;

import java.util.List;

public interface ChangeCodeService extends IService<ChangeCode> {
    void batchInsertChangeCode(List<ChangeCode> changeCodeList);

    IPage<ChangeCodeDTO> queryByDTO(ChangeCodeQueryDTO changeCodeQueryDTO);
}
