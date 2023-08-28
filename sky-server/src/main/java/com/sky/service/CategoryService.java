package com.sky.service;

import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageBean;

import java.util.List;

public interface CategoryService {

    //分页查询
    PageBean<Category> findByPage(CategoryPageQueryDTO dto);

    //分类保存
    void save(Category category);

    //删除
    void delete(Long id);

    //根据type值查询分类信息
    List<Category> findByType(Integer type);
}
