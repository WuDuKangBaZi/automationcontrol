package com.felixstudio.automationcontrol.service.dept;

import com.felixstudio.automationcontrol.entity.depart.Departments;
import com.felixstudio.automationcontrol.mapper.dept.DepartmentsMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentsService {
    private final DepartmentsMapper departmentsMapper;

    public DepartmentsService(DepartmentsMapper departmentsMapper) {
        this.departmentsMapper = departmentsMapper;
    }
    public List<Departments> getAllDepartments() {
        return departmentsMapper.selectList(null);
    }

    public int addDepartment(Departments departments) {
        return departmentsMapper.insert(departments);
    }

    public int updateDepartment(Departments departments) {
        return departmentsMapper.updateById(departments);
    }

    public int deleteDepartment(Long id) {
        return departmentsMapper.deleteById(id);
    }
}
