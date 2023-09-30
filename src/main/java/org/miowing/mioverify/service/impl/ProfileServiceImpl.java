package org.miowing.mioverify.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.miowing.mioverify.dao.ProfileDao;
import org.miowing.mioverify.pojo.Profile;
import org.miowing.mioverify.service.ProfileService;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import java.util.LinkedList;
import java.util.List;

@Service
public class ProfileServiceImpl extends ServiceImpl<ProfileDao, Profile> implements ProfileService {
    @Override
    public List<Profile> getByUserId(String userId) {
        LambdaQueryWrapper<Profile> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Profile::getBindUser, userId);
        return list(lqw);
    }
    @Override
    public @Nullable Profile getByName(String name) {
        LambdaQueryWrapper<Profile> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Profile::getName, name);
        return getOne(lqw);
    }
    @Override
    public List<Profile> getByNames(String[] names) {
        List<Profile> profiles = new LinkedList<>();
        for (String name : names) {
            Profile p = getByName(name);
            if (p != null) {
                profiles.add(p);
            }
        }
        return profiles;
    }
    @Override
    public List<Profile> getBySkinHash(String hash) {
        LambdaQueryWrapper<Profile> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Profile::getSkinHash, hash);
        return list(lqw);
    }
    @Override
    public List<Profile> getByCapeHash(String hash) {
        LambdaQueryWrapper<Profile> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Profile::getCapeHash, hash);
        return list(lqw);
    }
}