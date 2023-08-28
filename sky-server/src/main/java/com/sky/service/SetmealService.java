package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageDTO;
import com.sky.result.PageBean;
import com.sky.vo.SetmealVO;

public interface SetmealService {

    //分页查询
    PageBean<SetmealVO> findByPage(SetmealPageDTO setmealPageDTO);
    //新增
    void save(SetmealDTO setmealDTO);
    //主键查询
    SetmealVO findById(Long id);
    //更新
    void update(SetmealDTO setmealDTO);
}
