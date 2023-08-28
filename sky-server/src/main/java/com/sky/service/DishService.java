package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageDTO;
import com.sky.entity.Dish;
import com.sky.result.PageBean;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {

    //分页查询
    PageBean findByPage(DishPageDTO dto);

    //保存
    void save(DishDTO dto);

    //主键查询
    DishVO findById(Long id);

    //更新
    void update(DishDTO dto);

    //批量删除
    void deleteByIds(List<Long> ids);

    //根据name\categoryId\status查询
    List<DishVO> findList(DishPageDTO dto);
}
