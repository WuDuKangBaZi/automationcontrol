package com.felixstudio.automationcontrol.service.auth;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.felixstudio.automationcontrol.dto.auth.UsersDTO;
import com.felixstudio.automationcontrol.entity.auth.Users;
import com.felixstudio.automationcontrol.entity.depart.Departments;
import com.felixstudio.automationcontrol.entity.depart.UserDepartments;
import com.felixstudio.automationcontrol.mapper.auth.UsersMapper;
import com.felixstudio.automationcontrol.mapper.dept.UserDepartmentsMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UsersService {
    private final UsersMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserDepartmentsMapper userDepartmentsMapper;

    public UsersService(UsersMapper userMapper, PasswordEncoder passwordEncoder, UserDepartmentsMapper userDepartmentsMapper) {
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.userDepartmentsMapper = userDepartmentsMapper;
    }

    /**
     * 登录逻辑
     */
    public Optional<Users> validateLogin(String account, String rawPassword) {
        Users user = userMapper.selectOne(
                new LambdaQueryWrapper<Users>().eq(Users::getAccount, account)
        );
        if (user == null) return Optional.empty();

        boolean matches = passwordEncoder.matches(rawPassword, user.getPassword());
        return matches ? Optional.of(user) : Optional.empty();
    }

    /**
     * 注册新用户（密码加密后存储）
     */
    public boolean register(Users user,int deptId) {
        if(user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        // 可以设置默认状态、创建时间等
        if (user.getStatus() == null) {
            user.setStatus(1); // 1-正常
        }
        user.setCreatedTime(java.time.LocalDateTime.now());
        user.setUpdatedTime(java.time.LocalDateTime.now());
        try {
            if (userMapper.insert(user) > 0){
                // 新增成功后关联
                Users users = userMapper.selectOne(
                        new LambdaQueryWrapper<Users>().eq(Users::getAccount, user.getAccount()));
                UserDepartments userDepartments = new UserDepartments();
                userDepartments.setUserId(users.getId());
                userDepartments.setDepartmentId(deptId);
                userDepartmentsMapper.insert(userDepartments);
            }
        }catch (Exception e){
            // 失败后回滚插入
            return false;

        }
        // 插入数据库
        return true;
    }

    public int updateUser(Users user) {
        String pwd = user.getPassword();

        if (pwd != null) {
            if (pwd.isEmpty()) {
                user.setPassword(null); // 避免覆盖原密码
            } else {
                user.setPassword(passwordEncoder.encode(pwd));
            }
        }
       return userMapper.update(user, new LambdaQueryWrapper<Users>().eq(Users::getId, user.getId()));
    }

    public List<UsersDTO> listUsersByDept(Long deptId) {
        return userMapper.listUsersWithDept(deptId);
    }

    public boolean checkExists(String account) {
        Users user = userMapper.selectOne(
                new LambdaQueryWrapper<Users>().eq(Users::getAccount, account)
        );
        return user != null;
    }

    public int deleteByUserId(Long userId) {
        return userMapper.deleteById(userId);
    }

    public Users getUserByAccount(String account) {
        return userMapper.selectOne(
                new LambdaQueryWrapper<Users>().eq(Users::getAccount, account)
        );
    }

    public String getDepartmentNameByUserId(String userId) {
        Departments departments = this.userMapper.getUserDepartmentByUserId(userId);
        log.info(departments.toString());
        return departments != null ? departments.getDeptName() : null;
    }
}
