package com.sky.web.admin;

import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageBean;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@Slf4j
@RestController
@RequestMapping("/admin/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private JwtProperties jwtProperties;


    // 员工登录
    @PostMapping("/login")
    public Result login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        //1. 调用service登录
        Employee employee = employeeService.login(employeeLoginDTO);

        //2. 制作token
        Map<String, Object> claims = new HashMap<>();
        claims.put("empId", employee.getId());
        String token = JwtUtil.createJWT(jwtProperties.getAdminSecret(), jwtProperties.getAdminTtl(), claims);
        // 返回vo
        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .name(employee.getName())
                .token(token)
                .userName(employee.getUsername()).build();

        //3.返回结果
        return Result.success(employeeLoginVO);
    }

    // 员工退出
    @PostMapping("/logout")
    public Result logout() {
        return Result.success();
    }

    //请求参数
    //1. 负载提交的参数        @RequestBody
    //2. 请求路径/stu/1       @PathVariable
    //3. query               直接接收
    //请求路径一样  在controller中就应该使用一个方法处理
    @GetMapping("/page")
    public Result findByPage(EmployeePageQueryDTO dto) {
        //分页查询
        PageBean<Employee> page = employeeService.findByPage(dto);
        //返回结果
        return Result.success(page);
    }

    //保存
    @PostMapping()
    public Result save(@RequestBody Employee employee) {
        employeeService.save(employee);
        return Result.success();
    }

    //主键查询
    @GetMapping("/{id}")
    public Result findById(@PathVariable Long id) {
        Employee employee = employeeService.findById(id);//返回结果
        return Result.success(employee);
    }

    @PutMapping()
    public Result update(@RequestBody Employee employee) {
        employeeService.update(employee);
        return Result.success();
    }

    //启用  禁用
    @PostMapping("/status/{status}")
    public Result startOrStop(@PathVariable Integer status, Long id){
        //1. 创建对象参数
//        Employee employee = new Employee();
//        employee.setStatus(status);
//        employee.setId(id);

        Employee employee = Employee.builder()
                .status(status)
                .id(id)
                .build();

        //更新
        employeeService.update(employee);

        return Result.success();
    }

}
