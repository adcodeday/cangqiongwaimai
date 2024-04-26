package com.Lu.controller.admin;

import com.Lu.dto.DishDTO;
import com.Lu.dto.DishPageQueryDTO;
import com.Lu.result.PageResult;
import com.Lu.result.Result;
import com.Lu.service.DishService;
import com.Lu.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/admin/dish")
@Api(tags = "菜品相关接口")
@Slf4j
public class DishController {
    @Autowired
    DishService dishService;
    @PostMapping
    @ApiOperation("新增菜品")
    public Result addDish(@RequestBody DishDTO dishDTO){
        log.info("新增菜品：{}",dishDTO.getName());
        try {
            dishService.addDish(dishDTO);
        } catch (Exception e) {
            return Result.error("新增菜品失败");
        }
        return Result.success();
    }
    @PutMapping
    @ApiOperation("修改菜品")
    public Result updateDish(@RequestBody DishDTO dishDTO){
        log.info("修改菜品：{}",dishDTO.getName());
        try {
            dishService.updateDish(dishDTO);
        }catch (Exception e){
            return Result.error("修改菜品失败");
        }
        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("分页查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        log.info("单页查询:{}，{}",dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        try {
            PageResult pageResult=dishService.page(dishPageQueryDTO);
            return Result.success(pageResult);
        } catch (Exception e) {
            return Result.error("单页查询失败");
        }
    }
    @GetMapping("/list")
    @ApiOperation("按照分类id查询")
    public Result<List<DishVO>> list(DishDTO dishDTO){
        DishPageQueryDTO dishPageQueryDTO=new DishPageQueryDTO();
        dishPageQueryDTO.setCategoryId(Math.toIntExact(dishDTO.getCategoryId()));
        dishPageQueryDTO.setPageSize(99999);
        dishPageQueryDTO.setPage(1);
        try {
            Result<PageResult> pageResult = page(dishPageQueryDTO);
            PageResult data = pageResult.getData();
            return Result.success(data.getRecords());
        } catch (Exception e) {
            return Result.error("查询失败");
        }
    }
    @PostMapping("/status/{status}")
    @ApiOperation("起售禁售菜品")
    public Result status(@PathVariable int status,long id){
        log.info("修改id为：{}的菜品：{}",id,status);
        DishDTO dishDTO=new DishDTO();
        dishDTO.setStatus(status);
        dishDTO.setId(id);
        try {
            dishService.status(dishDTO);
            return Result.success();
        } catch (Exception e) {
            return Result.error("菜品修改失败");
        }
    }
    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品")
    public Result<DishVO> queryById(@PathVariable long id){
        try {
            //把id封装进dish
            DishDTO dishDTO=new DishDTO();
            dishDTO.setId(id);
            DishVO dishVO=dishService.queryById(dishDTO);

            return Result.success(dishVO);
        }catch (Exception e){
            return Result.error("根据id查询菜品失败");
        }
    }
    @DeleteMapping
    @ApiOperation("批量删除菜品")
    public Result deleteDishes(@RequestParam List<Long> ids){
        log.info("菜品批量删除");
        try {
            dishService.deleteDishes(ids);
            return Result.success();
        } catch (Exception e) {
            return Result.error("批量删除菜品失败");
        }
    }
}
