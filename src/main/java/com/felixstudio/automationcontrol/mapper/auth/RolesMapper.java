package com.felixstudio.automationcontrol.mapper.auth;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.felixstudio.automationcontrol.entity.auth.Roles;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RolesMapper extends BaseMapper<Roles> {
}
