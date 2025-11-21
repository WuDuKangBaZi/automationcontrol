package com.felixstudio.automationcontrol.controller;

import com.felixstudio.automationcontrol.common.ApiResponse;
import com.felixstudio.automationcontrol.entity.depart.Departments;
import com.felixstudio.automationcontrol.service.auth.UsersService;
import com.felixstudio.automationcontrol.service.dept.DepartmentsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/dept")
public class DepartmentsController {

    private final DepartmentsService departmentsService;
    private final UsersService usersService;
    public DepartmentsController(DepartmentsService departmentsService, UsersService usersService) {
        this.departmentsService = departmentsService;
        this.usersService = usersService;
    }

    @GetMapping("/all")
    public ApiResponse<List<Departments>> getAllDepartments() {
        log.info("Fetching all departments");
        return ApiResponse.success(this.departmentsService.getAllDepartments());
    }
    @PostMapping("/add")
    public ApiResponse<?> addDepartment(@RequestBody Departments departments) {
        return ApiResponse.success(this.departmentsService.addDepartment(departments));
    }
    @PostMapping("/save")
    public ApiResponse<?> saveDepartment(@RequestBody Departments departments) {
        return ApiResponse.success(this.departmentsService.updateDepartment(departments));
    }
    @PostMapping("/choseDelete")
    public ApiResponse<?> choseDeleteDepartment(@RequestBody Departments departments) {
        return ApiResponse.success(usersService.listUsersByDept(departments.getId()).isEmpty());
    }
    @PostMapping("/delete")
    public ApiResponse<?> deleteDepartment(@RequestBody Departments departments) {
        return ApiResponse.success(this.departmentsService.deleteDepartment(departments.getId()));
    }
}
