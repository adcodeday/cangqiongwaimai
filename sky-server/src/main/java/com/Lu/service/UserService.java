package com.Lu.service;

import com.Lu.dto.UserLoginDTO;
import com.Lu.entity.User;
import com.Lu.result.Result;
import com.Lu.vo.UserLoginVO;

public interface UserService {
    /**
     * 登录
     */
    User login(UserLoginDTO userLoginDTO);
}
