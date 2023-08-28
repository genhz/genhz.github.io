package com.sky.web.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageDTO;
import com.sky.entity.Dish;
import com.sky.result.PageBean;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//菜品
@RestController
@RequestMapping("/admin/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    //分页查询
    @GetMapping("/page")
    public Result findByPage(DishPageDTO dto) {
        PageBean pageBean = dishService.findByPage(dto);
        return Result.success(pageBean);
    }

    //菜品保存
    @PostMapping()
    public Result save(@RequestBody DishDTO dto) {
        dishService.save(dto);
        return Result.success();
    }

    //菜品回显
    @GetMapping("/{id}")
    public Result findById(@PathVariable Long id) {
        DishVO dishVO = dishService.findById(id);
        return Result.success(dishVO);
    }

    //修改菜品
    @PutMapping()
    public Result update(@RequestBody DishDTO dto) {
        dishService.update(dto);
        return Result.success();
    }

    //当使用集合接收参数的时候,需要添加@RequestParam注解
    //删除菜品
    @DeleteMapping("")
    public Result deleteByIds(@RequestParam List<Long> ids) {
        dishService.deleteByIds(ids);
        return Result.success();
    }

    //根据name\categoryId\status查询
    @GetMapping("/list")
    public Result findList(DishPageDTO dto) {
        List<DishVO> dishList = dishService.findList(dto);
        return Result.success(dishList);
    }

}
