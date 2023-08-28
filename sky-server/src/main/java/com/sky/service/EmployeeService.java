package com.sky.service;

import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageBean;

public interface EmployeeService {

    // 员工登录
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    //分页查询
    PageBean<Employee> findByPage(EmployeePageQueryDTO dto);

    //添加员工
    void save(Employee employee);

    //主键查询
    Employee findById(Long id);

    //修改
    void update(Employee employee);
}
