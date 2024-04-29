package com.Lu.mapper;

import com.Lu.annocation.AutoFill;
import com.Lu.constant.AutoFillConstant;
import com.Lu.dto.DishDTO;
import com.Lu.dto.DishPageQueryDTO;
import com.Lu.entity.Dish;
import com.Lu.enumeration.OperationType;
import com.Lu.vo.DishVO;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {
    Page<DishVO> page(DishPageQueryDTO dishPageQueryDTO);

    @AutoFill(value = OperationType.INSERT)
    void addDish(Dish dish);
    @AutoFill(value =OperationType.UPDATE)
    void updateDish(Dish dish);
    DishVO selectOne(Dish dish);

    void deleteDishes(List<Long> ids);
    List<DishVO> list(Dish dish);
    @Select("select * from dish d where d.id in " +
            "(select dish_id from setmeal_dish where setmeal_id=#{id})"
    )
    List<Dish> getBySetmealId(Long id);
}
