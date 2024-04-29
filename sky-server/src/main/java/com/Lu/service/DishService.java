package com.Lu.service;

import com.Lu.dto.DishDTO;
import com.Lu.dto.DishPageQueryDTO;
import com.Lu.entity.Dish;
import com.Lu.result.PageResult;
import com.Lu.vo.DishVO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface DishService {
    void addDish(DishDTO dishDTO);

    void updateDish(DishDTO dishDTO);

    PageResult page(DishPageQueryDTO dishPageQueryDTO);

    void status(DishDTO dishDTO);

    DishVO queryById(DishDTO dishDTO);


    void deleteDishes(List<Long> ids);

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    List<DishVO> listWithFlavor(Dish dish);
}
