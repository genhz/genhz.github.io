package com.sky.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.exception.BusinessException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageBean;
import com.sky.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    @Override
    public PageBean<Category> findByPage(CategoryPageQueryDTO dto) {
        //1. 设置分页条件
        Page<Category> page = new Page<>(dto.getPage(), dto.getPageSize());

        //2. 设置业务条件
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotEmpty(dto.getName()), Category::getName, dto.getName());
        wrapper.eq(dto.getType() != null, Category::getType, dto.getType());
        wrapper.orderByAsc(Category::getSort);

        //3. 执行分页查询
        categoryMapper.selectPage(page, wrapper);

        //4. 返回结果
        return new PageBean<Category>(page.getTotal(), page.getRecords());
    }

    @Override
    public void save(Category category) {
        category.setStatus(1);//在售
        categoryMapper.insert(category);
    }

    @Override
    public void delete(Long id) {
        //分类id
        //1. 先判断当前分类id下是否有具体的菜品,如果有,就要提示不能删除
        //select count(*) from dish where category_id = 12;
        int count1 = dishMapper.countByCategoryId(id);
        if (count1 > 0) {
            //就要提示不能删除
            throw new BusinessException("当前分类下存在菜品,不能删除");
        }

        //2. 再判断当前分类id下是否有具体的套餐,如果有,就要提示不能删除
        //select count(*) from setmeal where category_id = 12;
        int count2 = setmealMapper.countByCategoryId(id);
        if (count2 > 0) {
            //就要提示不能删除
            throw new BusinessException("当前分类下存在套餐,不能删除");
        }

        //3. 直接调用categoryMapper安装id删除
        categoryMapper.deleteById(id);
    }

    @Override
    public List<Category> findByType(Integer type) {
        //1. 创建查询条件封装器
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(type != null, Category::getType, type);

        return categoryMapper.selectList(wrapper);
    }
}
