package com.Lu.mapper;

import com.Lu.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface SetmealDishMapper {
    List<Long> selectByDishIds(List<Long> ids);
}
