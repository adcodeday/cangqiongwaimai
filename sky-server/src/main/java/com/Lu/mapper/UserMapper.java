package com.Lu.mapper;

import com.Lu.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    /**
     * 根据openid查询用户
     * @param openid
     * @return
     */
    @Select("select * from user where openid=#{openid}")
    User selectByOpenId(String openid);

    /**
     * 插入user
     * @param build
     * @return
     */
    Integer insert(User build);
}
