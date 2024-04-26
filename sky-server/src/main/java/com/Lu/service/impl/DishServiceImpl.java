package com.Lu.service.impl;

import com.Lu.constant.StatusConstant;
import com.Lu.dto.DishDTO;
import com.Lu.dto.DishPageQueryDTO;
import com.Lu.entity.Dish;
import com.Lu.entity.DishFlavor;
import com.Lu.entity.Employee;
import com.Lu.exception.DeletionNotAllowedException;
import com.Lu.mapper.DishFlavorMapper;
import com.Lu.mapper.DishMapper;
import com.Lu.mapper.SetmealDishMapper;
import com.Lu.mapper.SetmealMapper;
import com.Lu.result.PageResult;
import com.Lu.service.DishService;
import com.Lu.vo.DishVO;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;

//DTO和VO只能在service层或之上
//entity只能存在于service层或之下
@Service
@Slf4j
public class DishServiceImpl implements DishService {
    @Autowired
    DishMapper dishMapper;
    @Autowired
    DishFlavorMapper dishFlavorMapper;
    @Autowired
    SetmealDishMapper setmealDishMapper;
    @Override
    public void addDish(DishDTO dishDTO) {
        //把dishDto对象转换为dish对象，并储存flavors
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        //对dish表进行操作，并把id值存入对象
        dishMapper.addDish(dish);
        //对dish-flavor进行操作，把id存入dish_id
        List<DishFlavor> flavors = dishDTO.getFlavors();

        if(flavors!=null&& !flavors.isEmpty()){
            for (int i = 0; i < flavors.size(); i++) {
                flavors.get(0).setDishId(dish.getId());
            }
            dishFlavorMapper.addDish(flavors);
        }
    }

    @Override
    public void updateDish(DishDTO dishDTO) {
        //把dishDto对象转换为dish对象，并储存flavors
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        //对dish表进行操作，并把id值存入对象
        dishMapper.updateDish(dish);
        //对dish-flavor进行操作，把id存入dish_id
        List<DishFlavor> flavors = dishDTO.getFlavors();
        dishFlavorMapper.deleteAllDishFlavor(dishDTO.getId());
        dishFlavorMapper.updateDish(flavors);

    }

    @Override
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO> dishPage=dishMapper.page(dishPageQueryDTO);
        long totol=dishPage.getTotal();
        List<DishVO> dishes=dishPage.getResult();
        return new PageResult(totol,dishes);
    }

    @Override
    public void status(DishDTO dishDTO) {
        Dish dish=new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.updateDish(dish);
    }

    @Override
    public DishVO queryById(DishDTO dishDTO) {
        Dish dish=new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        DishVO dishVO=new DishVO();
        //TODO 出错
        BeanUtils.copyProperties(dishMapper.selectOne(dish),dishVO);
        dishVO.setFlavors(dishFlavorMapper.selectByDishId(dish.getId()));
        return dishVO;
    }

    @Override
    public void deleteDishes(List<Long> ids) {
        for (int i = 0; i < ids.size(); i++) {
            Dish dish=new Dish();
            dish.setId(ids.get(i));
            DishVO dishVO=dishMapper.selectOne(dish);
            if(dishVO.getStatus().equals(StatusConstant.ENABLE)){
                throw new DeletionNotAllowedException("因菜品正在售卖，不可删除");
            }
        }
        for (int i = 0; i < ids.size(); i++) {
            List<Long> longs = setmealDishMapper.selectByDishIds(ids);
            if(!longs.isEmpty()){
                throw new DeletionNotAllowedException("因菜品关联套餐，不可删除");
            }
        }
        dishMapper.deleteDishes(ids);
        for (int i = 0; i < ids.size(); i++) {
            dishFlavorMapper.deleteAllDishFlavor(ids.get(i));
        }
    }
}
