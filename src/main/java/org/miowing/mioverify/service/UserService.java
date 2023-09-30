package org.miowing.mioverify.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.miowing.mioverify.pojo.User;

public interface UserService extends IService<User> {
    User getLogin(String username, String password);
    User getLogin(String username, String password, boolean exception);
    User getLoginNoPwd(String username);
}