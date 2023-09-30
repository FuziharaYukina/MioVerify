package org.miowing.mioverify.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.miowing.mioverify.pojo.Profile;
import java.util.List;

public interface ProfileService extends IService<Profile> {
    List<Profile> getByUserId(String userId);
    Profile getByName(String name);
    List<Profile> getByNames(String[] names);
    List<Profile> getBySkinHash(String hash);
    List<Profile> getByCapeHash(String hash);
}