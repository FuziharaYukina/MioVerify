package org.miowing.mioverify.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.miowing.mioverify.pojo.Profile;

@Mapper
public interface ProfileDao extends BaseMapper<Profile> {
}