package com.felixstudio.automationcontrol.mapper.auth;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.felixstudio.automationcontrol.entity.auth.UserRoles;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserRolesMapper extends BaseMapper<UserRoles> {
    @Select("""
    select r.role_name from roles r
    left join user_roles ur on r.id = ur.role_id
    where ur.user_id = #{id}
""")
    List<String> getRoleNamesByUserId(Long id);
    @Insert("""
                <script>
                insert into user_roles(user_id, role_id)
                        values
                        <foreach collection="roleIds" item="roleId" separator=",">
                            (#{userId}, #{roleId})
                        </foreach>
                </script>
            """)
    int insertUserRoles(@Param("userId") Long userId,@Param("roleIds") List<Long> roleIds);
}
