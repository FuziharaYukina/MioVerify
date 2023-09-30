package org.miowing.mioverify.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.miowing.mioverify.pojo.User;

@Mapper
public interface UserDao extends BaseMapper<User> {
}