package com.sky.web.admin;

import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageBean;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类管理
 */
@Slf4j
@RestController
@RequestMapping("/admin/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    //分页查询
    @GetMapping("/page")
    public Result findByPage(CategoryPageQueryDTO dto){
        PageBean<Category> pageBean = categoryService.findByPage(dto);
        return Result.success(pageBean);
    }

    //保存分类信息
    @PostMapping()
    public Result save(@RequestBody Category category){
        categoryService.save(category);
        return Result.success();
    }

    //删除分类
    @DeleteMapping()
    public Result delete(Long id){
        categoryService.delete(id);
        return Result.success();
    }

    //根据type值查询分类信息
    @GetMapping("/list")
    public Result findByType(Integer type){
        List<Category> categoryList = categoryService.findByType(type);
        return Result.success(categoryList);
    }

}
