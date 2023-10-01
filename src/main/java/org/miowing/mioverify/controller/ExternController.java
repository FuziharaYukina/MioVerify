package org.miowing.mioverify.controller;

import lombok.extern.slf4j.Slf4j;
import org.miowing.mioverify.exception.DuplicateProfileNameException;
import org.miowing.mioverify.exception.DuplicateUserNameException;
import org.miowing.mioverify.exception.FeatureNotSupportedException;
import org.miowing.mioverify.pojo.Profile;
import org.miowing.mioverify.pojo.User;
import org.miowing.mioverify.pojo.request.ProfileRegisterReq;
import org.miowing.mioverify.pojo.request.UserRegisterReq;
import org.miowing.mioverify.service.ProfileService;
import org.miowing.mioverify.service.UserService;
import org.miowing.mioverify.util.DataUtil;
import org.miowing.mioverify.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * External API for registration of accounts
 */
@RestController
@RequestMapping("/extern")
@Slf4j
public class ExternController {
    @Autowired
    private UserService userService;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private DataUtil dataUtil;
    @PostMapping("/register/user")
    public ResponseEntity<?> registerUser(@RequestBody UserRegisterReq req) {
        if (!dataUtil.isAllowRegister()) {
            throw new FeatureNotSupportedException();
        }
        if (!dataUtil.isAllowRegUser()) {
            throw new FeatureNotSupportedException();
        }
        if (dataUtil.isUsePermKey() && !dataUtil.getPermKey().equals(req.getKey())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (userService.getLoginNoPwd(req.getUsername()) != null) {
            throw new DuplicateUserNameException();
        }
        User user = new User()
                .setId(Util.genUUID())
                .setPassword(req.getPassword())
                .setUsername(req.getUsername())
                .setPreferredLang(req.getPreferredLang());
        log.info("New user register: {}", user.getUsername());
        userService.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("/register/profile")
    public ResponseEntity<?> registerProfile(@RequestBody ProfileRegisterReq req) {
        if (!dataUtil.isAllowRegister()) {
            throw new FeatureNotSupportedException();
        }
        if (!dataUtil.isAllowRegProfile()) {
            throw new FeatureNotSupportedException();
        }
        if (dataUtil.isUsePermKey() && !dataUtil.getPermKey().equals(req.getKey())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (!dataUtil.isMultiProfileName()) {
            if (profileService.getByName(req.getProfileName()) != null) {
                throw new DuplicateProfileNameException();
            }
        }
        User user;
        if (dataUtil.isProfileStrict()) {
            user = userService.getLogin(req.getUsername(), req.getPassword());
        } else {
            user = userService.getLoginNoPwd(req.getUsername());
        }
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Profile profile = new Profile()
                .setId(Util.genUUID())
                .setName(req.getProfileName())
                .setBindUser(user.getId())
                .setSkinUpAllow(req.isSkinUploadAllow())
                .setCapeUpAllow(req.isCapeUploadAllow());
        log.info("New profile register: {}", profile.getName() + " (" + user.getUsername() + ")");
        profileService.save(profile);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}