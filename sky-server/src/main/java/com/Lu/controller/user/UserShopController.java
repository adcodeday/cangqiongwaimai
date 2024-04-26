package com.Lu.controller.user;
import com.Lu.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "店铺相关接口")
@Slf4j
@RequestMapping("/user/shop")
public class UserShopController {
    @Autowired
    RedisTemplate redisTemplate;
    @GetMapping("/status")
    @ApiOperation("查询店铺状态")
    public Result<Integer> getStatus(){
        log.info("员工端查询店铺状态");
        try {
            Integer shopStatus = (Integer)redisTemplate.opsForValue().get("SHOP_STATUS");
            return Result.success(shopStatus);
        } catch (Exception e) {
            return Result.error("员工查询店铺状态失败");
        }
    }
}
