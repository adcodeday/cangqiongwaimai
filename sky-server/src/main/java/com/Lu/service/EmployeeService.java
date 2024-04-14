package com.Lu.service;

import com.Lu.dto.EmployeeLoginDTO;
import com.Lu.entity.Employee;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

}
