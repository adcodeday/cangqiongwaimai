package com.Lu.service.impl;

import com.Lu.constant.MessageConstant;
import com.Lu.constant.PasswordConstant;
import com.Lu.constant.StatusConstant;
import com.Lu.context.BaseContext;
import com.Lu.dto.EmployeeDTO;
import com.Lu.dto.EmployeeLoginDTO;
import com.Lu.dto.EmployeePageQueryDTO;
import com.Lu.entity.Employee;
import com.Lu.exception.AccountLockedException;
import com.Lu.exception.AccountNotFoundException;
import com.Lu.exception.PasswordErrorException;
import com.Lu.mapper.EmployeeMapper;
import com.Lu.result.PageResult;
import com.Lu.service.EmployeeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.poi.util.PackageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    // TODO 对密码进行加密处理
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        // TODO 后期需要进行md5加密，然后再进行比对
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }
    /*
    * 创建员工
    * */
    @Override
    public void save(EmployeeDTO employeeDTO) {
        Employee employee=new Employee();
        //对象属性拷贝
        BeanUtils.copyProperties(employeeDTO,employee);
        //防止硬编码
        employee.setStatus(StatusConstant.ENABLE);
        //md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes(StandardCharsets.UTF_8)));
//        //创建时间和修改时间
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        //当前记录创建人id
//        //TODO 写出创建人以及创建时间
//        employee.setCreateUser(BaseContext.getCurrentId());
//        employee.setUpdateUser(BaseContext.getCurrentId());
        employeeMapper.insert(employee);
    }

    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        //开始分页查询
        PageHelper.startPage(employeePageQueryDTO.getPage(),employeePageQueryDTO.getPageSize());
        Page<Employee> page=employeeMapper.pageQuery(employeePageQueryDTO);
        Long total=page.getTotal();
        List<Employee> result = page.getResult();
        return new PageResult(total,result);
    }

    @Override
    public void startOrStop(Integer status, long id) {
        //发update SQL
//        Employee employee=new Employee();
//        employee.setStatus(status);
//        employee.setId(id);
        //build方法创建employee
        Employee employee = Employee.builder()
                .id(id)
                .status(status).build();
        employeeMapper.update(employee);
    }

    /**
     * 根据员工id查询
     * @param id
     * @return
     */
    @Override
    public Employee queryById(int id) {
        Employee employee=employeeMapper.queryById(id);
        employee.setPassword("****");
        return employee;
    }

    @Override
    public void update(EmployeeDTO employeeDTO) {
        Employee employee=new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);
        employeeMapper.update(employee);
    }


}
