package com.Lu.controller.user;

import com.Lu.constant.JwtClaimsConstant;
import com.Lu.dto.UserLoginDTO;
import com.Lu.entity.User;
import com.Lu.mapper.UserMapper;
import com.Lu.properties.JwtProperties;
import com.Lu.result.Result;
import com.Lu.service.UserService;
import com.Lu.utils.JwtUtil;
import com.Lu.vo.UserLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/user/user")
@Api(tags = "用户模块")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    JwtProperties jwtProperties;

    /**
     * 微信登录模块
     * @param userLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("用户登录")
    public Result<UserLoginVO> login(@RequestBody UserLoginDTO userLoginDTO){
        User login = userService.login(userLoginDTO);
        Map<String, Object> claims=new HashMap<>();
        claims.put(JwtClaimsConstant.USER_ID,login.getId());
        //jwt令牌是一个token，就是本地服务器并不保存该令牌的信息，而是在客户端保存，本地只保存令牌的解密方式，客户端发给服务器，服务器看看能不能解密，解密后的信息包含了claims参数数据，和ttl过期时间
        String jwt = JwtUtil.createJWT(jwtProperties.getUserSecretKey(), jwtProperties.getUserTtl(), claims);
        UserLoginVO build = UserLoginVO.builder()
                .id(login.getId())
                .openid(login.getOpenid())
                .token(jwt)
                .build();
        return Result.success(build);
    }
}
