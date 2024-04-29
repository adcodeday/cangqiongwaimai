package com.Lu.service;

import com.Lu.dto.SetmealDTO;
import com.Lu.dto.SetmealPageQueryDTO;
import com.Lu.entity.Dish;
import com.Lu.result.PageResult;
import com.Lu.vo.DishItemVO;
import com.Lu.vo.SetmealVO;

import java.util.List;

public interface SetmealService {
    void saveWithDish(SetmealDTO setmealDTO);
    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    void deleteBatch(List<Long> ids);

    SetmealVO getByIdWithDish(Long id);

    void startOrStop(Integer status, Long id);
    void update(SetmealDTO setmealDTO);

    List<DishItemVO> getDishItemById(Long id);
}
