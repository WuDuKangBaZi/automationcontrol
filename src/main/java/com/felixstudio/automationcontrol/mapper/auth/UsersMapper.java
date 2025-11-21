package com.felixstudio.automationcontrol.mapper.auth;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.felixstudio.automationcontrol.dto.auth.UsersDTO;
import com.felixstudio.automationcontrol.entity.auth.Users;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UsersMapper extends BaseMapper<Users> {
    List<UsersDTO> listUsersWithDept(Long deptId);
}
