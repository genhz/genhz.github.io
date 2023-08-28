package com.sky.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sky.entity.Dish;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {

    @Select("select count(*) from dish where category_id = #{categoryId}")
    int countByCategoryId(Long categoryId);

    //菜品和分类
    @Select("select d.*,c.name as categoryName from dish d left join category c on d.category_id = c.id where d.id = #{id}")
    DishVO findById(Long id);
}
