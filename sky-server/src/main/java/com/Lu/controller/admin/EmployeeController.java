package com.Lu.controller.admin;

import com.Lu.constant.JwtClaimsConstant;
import com.Lu.dto.EmployeeDTO;
import com.Lu.dto.EmployeeLoginDTO;
import com.Lu.dto.EmployeePageQueryDTO;
import com.Lu.entity.Employee;
import com.Lu.properties.JwtProperties;
import com.Lu.result.PageResult;
import com.Lu.result.Result;
import com.Lu.service.EmployeeService;
import com.Lu.utils.JwtUtil;
import com.Lu.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工相关接口")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @ApiOperation(value = "员工登录")
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation("员工退出")
    public Result<String> logout() {
        return Result.success();
    }

    @PostMapping
    @ApiOperation("新增员工")
    public Result save(@RequestBody EmployeeDTO employeeDTO){
        log.info("新增员工：{}",employeeDTO);
        employeeService.save(employeeDTO);
        return Result.success();
    }
    /*
        单页查询
     */
    @GetMapping("/page")
    @ApiOperation("单页查询")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO){

        log.info("单页查询：页号：{},页面容量：{}",employeePageQueryDTO.getPage(),employeePageQueryDTO.getPageSize());
        PageResult pageResult=employeeService.pageQuery(employeePageQueryDTO);
         return Result.success(pageResult);
    }
    //查询加上泛型，不是查询的不用加泛型，因为只需要返回状态码
    /**
     * 启用禁用员工账号
     */

    @PostMapping("/status/{status}")//路径参数需要带该注解
    @ApiOperation("启用禁用员工账号")
    public Result startOrStop(@PathVariable  Integer status,long id){
        log.info("启用禁用员工账号:{},{}",status,id);
        employeeService.startOrStop(status,id);
        return Result.success();
    }

    /**
     * 根据id查询员工
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询员工")
    public Result<Employee> queryById(@PathVariable int id){
        Employee employee = employeeService.queryById(id);
        return Result.success(employee);
    }
    @PutMapping("")
    @ApiOperation("更新员工信息")
    public void update(@RequestBody EmployeeDTO employeeDTO){
        log.info("更新员工信息");
        employeeService.update(employeeDTO);
    }
}
