package com.sky.web.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageBean;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    //分页查询
    @GetMapping("/page")
    public Result findByPage(SetmealPageDTO setmealPageDTO) {
        PageBean<SetmealVO> pageBean = setmealService.findByPage(setmealPageDTO);
        return Result.success(pageBean);
    }

    //新增
    @PostMapping
    public Result save(@RequestBody SetmealDTO setmealDTO){
        setmealService.save(setmealDTO);
        return Result.success();
    }

    //主键查询
    @GetMapping("/{id}")
    public Result findById(@PathVariable Long id){
        SetmealVO setmealVO = setmealService.findById(id);
        return Result.success(setmealVO);
    }

    //修改
    @PutMapping
    public Result update(@RequestBody SetmealDTO setmealDTO){
        setmealService.update(setmealDTO);
        return Result.success();
    }
}
