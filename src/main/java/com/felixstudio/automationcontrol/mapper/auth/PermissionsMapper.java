package com.felixstudio.automationcontrol.mapper.auth;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PermissionsMapper {
    @Select(
            """
                    select p.perm_name from permissions p
                    left join role_permissions rp on p.id = rp.permission_id
                    left join user_roles ur on rp.role_id = ur.role_id
                    where ur.user_id = #{id}
                    """
    )
    List<String> getPermissionNamesByUserId(Long id);
}
