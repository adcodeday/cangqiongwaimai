package com.Lu.service.impl;

import com.Lu.constant.MessageConstant;
import com.Lu.dto.UserLoginDTO;
import com.Lu.entity.User;
import com.Lu.exception.LoginFailedException;
import com.Lu.mapper.UserMapper;
import com.Lu.properties.WeChatProperties;
import com.Lu.service.UserService;
import com.Lu.utils.HttpClientUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    WeChatProperties weChatProperties;
    //微信服务接口地址
    public static final String wxlogin="https://api.weixin.qq.com/sns/jscode2session";
    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    @Override
    public User login(UserLoginDTO userLoginDTO) {
        //调用微信接口服务
        String openid = getOpenId(userLoginDTO.getCode());
        //判断微信id是否为空
        if(openid==null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        //合法微信用户，判断是否是新用户，判断openid在不在用户表里
        User user = userMapper.selectByOpenId(openid);
        if (user==null){
            user = User.builder().openid(openid).createTime(LocalDateTime.now()).build();
            userMapper.insert(user);
        }
        //是新用户，自动完成注册
        return user;
    }

    /**
     * 调用微信接口服务，获取微信用户openid
     */
    private String getOpenId(String code){
        Map<String, String> map=new HashMap<>();
        map.put("appid",weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("js_code",code);
        map.put("grant_type","authorization_code");
        String json = HttpClientUtil.doGet(wxlogin, map);
        JSONObject jsonObject = JSON.parseObject(json);
        String openid = jsonObject.getString("openid");
        return openid;
    }
}
