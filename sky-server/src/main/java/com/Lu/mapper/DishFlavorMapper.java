package com.Lu.mapper;

import com.Lu.dto.DishDTO;
import com.Lu.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {
    void addDish(List<DishFlavor> dishFlavors);

    void updateDish(List<DishFlavor> flavors);
    @Delete("delete from dish_flavor where dish_id=#{dishId}")
    void deleteAllDishFlavor(long dishId);
    List<DishFlavor> selectByDishId(long dishId);
}
