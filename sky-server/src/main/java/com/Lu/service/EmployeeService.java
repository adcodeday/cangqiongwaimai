package com.Lu.service;

import com.Lu.dto.EmployeeDTO;
import com.Lu.dto.EmployeeLoginDTO;
import com.Lu.dto.EmployeePageQueryDTO;
import com.Lu.entity.Employee;
import com.Lu.result.PageResult;

import java.util.List;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    void save(EmployeeDTO employeeDTO);

    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    void startOrStop(Integer status, long id);

    /**
     * 根据id查询员工
     * @param id
     * @return
     */
    Employee queryById(int id);

    /**
     * 根据id更新员工信息
     * @param employeeDTO
     */
    void update(EmployeeDTO employeeDTO);
}
