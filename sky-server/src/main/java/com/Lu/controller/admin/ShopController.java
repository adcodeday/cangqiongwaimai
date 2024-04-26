package com.Lu.controller.admin;

import com.Lu.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/shop")
@Api(tags = "店铺相关接口")
@Slf4j
public class ShopController {
    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 设置店铺状态
     * @param status
     * @return
     */
    @PutMapping("/{status}")
    @ApiOperation("设置店铺状态")
    public Result setShopStatus(@PathVariable int status){
        log.info("设置店铺状态{}",status==1?"营业":"关门");
        try {
            redisTemplate.opsForValue().set("SHOP_STATUS",status);
            return Result.success();
        } catch (Exception e) {
            return Result.error("修改营业状态失败");
        }
    }

    /**
     * 获取店铺营业状态
     * @param
     * @return
     */
    @GetMapping("/status")
    @ApiOperation("获取店铺状态")
    public Result<Integer> getShopStatus(){
        try {
            Integer shopStatus = (Integer)redisTemplate.opsForValue().get("SHOP_STATUS");
            log.info("店铺状态为{}",shopStatus.equals(1)?"营业":"关门");
            return Result.success(shopStatus);
        } catch (Exception e) {
            return Result.error("获取店铺营业状态失败");
        }
    }
}
