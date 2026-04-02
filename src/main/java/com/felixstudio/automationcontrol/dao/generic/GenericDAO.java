package com.felixstudio.automationcontrol.dao.generic;

import com.felixstudio.automationcontrol.entity.genericRevamp.GeneralVersionEdit;
import lombok.Data;

@Data
public class GenericDAO extends GeneralVersionEdit {
    private Integer taskTotal;
    private Integer taskPending;
    private Integer taskRunning;
    private Integer taskSuccess;
    private Integer taskFail;


}
