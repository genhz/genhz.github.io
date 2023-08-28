package com.sky.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageDTO;
import com.sky.entity.Category;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.BusinessException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.result.PageBean;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Override
    public PageBean findByPage(DishPageDTO dto) {
        //1. 设置分页条件
        Page<Dish> page = new Page<>(dto.getPage(), dto.getPageSize());

        //2. 设置业务条件
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotEmpty(dto.getName()), Dish::getName, dto.getName())
                .eq(dto.getCategoryId() != null, Dish::getCategoryId, dto.getCategoryId())
                .eq(dto.getStatus() != null, Dish::getStatus, dto.getStatus())
                .orderByDesc(Dish::getUpdateTime);

        //3. 执行查询
        dishMapper.selectPage(page, wrapper);
        List<Dish> records = page.getRecords();

        List<DishVO> dishVOList = new ArrayList<>();//空集合

        for (Dish dish : records) {
            //DishVO dishVO = new DishVO();
            //1. dish-->dishVo
            DishVO dishVO = BeanUtil.copyProperties(dish, DishVO.class);

            //2. 想办法把dishVo中categoryName值赋上
            //根据分类id查询分类表,内容中包含分类名称
            Category category = categoryMapper.selectById(dish.getCategoryId());
            dishVO.setCategoryName(category.getName());

            //3. dishVo->放到dishVOList
            dishVOList.add(dishVO);
        }

        //4. 返回结果
        return new PageBean(page.getTotal(), dishVOList);
    }

    @Override
    @Transactional
    public void save(DishDTO dto) {
        //1. 从dto中提取菜品的基本信息,保存dish表
        Dish dish = BeanUtil.copyProperties(dto, Dish.class);
        log.info("菜品保存之前:" + dish);
        dishMapper.insert(dish);//mp主键返回
        log.info("菜品保存之后:" + dish);

        //2. 从dto中提取菜品的口味列表,保存到口味表中
        List<DishFlavor> flavors = dto.getFlavors();
        for (DishFlavor dishFlavor : flavors) {
            //为每一个口味对象赋值菜品id
            dishFlavor.setDishId(dish.getId());

            //口味信息入库
            dishFlavorMapper.insert(dishFlavor);
        }
    }

    @Override
    public DishVO findById(Long id) {
        //1. 根据菜品id使用联合查询获取菜品信息和分类名称
        DishVO dishVO = dishMapper.findById(id);

        //2. 根据菜品id去口味表中查询口味列表
        List<DishFlavor> dishFlavorList = dishFlavorMapper.findByDishId(id);

        //3. 组装返回结果
        dishVO.setFlavors(dishFlavorList);
        return dishVO;
    }

    @Override
    @Transactional
    public void update(DishDTO dto) {
        //1. dto-->dish  根据id将菜品的基本信息更新到dish表
        Dish dish = BeanUtil.copyProperties(dto, Dish.class);
        dishMapper.updateById(dish);

        //2. 根据菜品的id,删除掉当前菜品的所有口味信息
        //delete from dish_flavor where dish_id = #{}
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId, dto.getId());
        dishFlavorMapper.delete(wrapper);//条件删除

        //3. dto-->新的口味列表,重新向口味表进行保存
        List<DishFlavor> flavors = dto.getFlavors();
        for (DishFlavor dishFlavor : flavors) {
            //为每一个口味对象赋值菜品id
            dishFlavor.setDishId(dish.getId());

            //口味信息入库
            dishFlavorMapper.insert(dishFlavor);
        }
    }

    //一个service方法中多次操作(增删改)了数据表,一定要控制事务
    @Override
    @Transactional
    public void deleteByIds(List<Long> ids) {
        for (Long id : ids) {
            //1. 根据id查询菜品信息
            Dish dish = dishMapper.selectById(id);

            //2. 判断status是否在售,如果是,直接抛异常停止
            if (dish.getStatus() == 1) {
                throw new BusinessException("要删除的菜品中有在售状态的,无法删除");
            }

            //todo 3. 被套餐关联的菜品不能删除

            //4. 根据菜品id删除菜品信息(dish)
            dishMapper.deleteById(id);

            //5. 根据菜品id删除菜品口味信息(dishFlavor)
            LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(DishFlavor::getDishId, id);
            dishFlavorMapper.delete(wrapper);
        }
    }

    //根据name\categoryId\status查询
    @Override
    public List<DishVO> findList(DishPageDTO dto) {
        //1. 构造查询条件
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotEmpty(dto.getName()), Dish::getName, dto.getName())
                .eq(dto.getCategoryId() != null, Dish::getCategoryId, dto.getCategoryId())
                .eq(dto.getStatus() != null, Dish::getStatus, dto.getStatus())
                .orderByDesc(Dish::getUpdateTime);

        //2. 执行查询,返回结果
        List<Dish> dishList = dishMapper.selectList(wrapper);

        //List<Dish>  --> List<DishVo>

        List<DishVO> dishVoList = new ArrayList<>();
        for (Dish dish : dishList) {
            //1. 构建DishVo
            DishVO dishVO = BeanUtil.copyProperties(dish, DishVO.class);

            //2. 补充分类名称
            Category category = categoryMapper.selectById(dish.getCategoryId());
            dishVO.setCategoryName(category.getName());

            //3. 放入vo集合
            dishVoList.add(dishVO);
        }

        return dishVoList;
    }
}
