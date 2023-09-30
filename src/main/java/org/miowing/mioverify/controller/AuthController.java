package org.miowing.mioverify.controller;

import lombok.extern.slf4j.Slf4j;
import org.miowing.mioverify.exception.InvalidTokenException;
import org.miowing.mioverify.exception.NoProfileException;
import org.miowing.mioverify.exception.ProfileMismatchException;
import org.miowing.mioverify.pojo.AToken;
import org.miowing.mioverify.pojo.Profile;
import org.miowing.mioverify.pojo.ProfileShow;
import org.miowing.mioverify.pojo.User;
import org.miowing.mioverify.pojo.request.AuthReq;
import org.miowing.mioverify.pojo.request.RefreshReq;
import org.miowing.mioverify.pojo.request.TokenGReq;
import org.miowing.mioverify.pojo.response.AuthResp;
import org.miowing.mioverify.service.ProfileService;
import org.miowing.mioverify.service.RedisService;
import org.miowing.mioverify.service.UserService;
import org.miowing.mioverify.util.TokenUtil;
import org.miowing.mioverify.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/authserver")
@Slf4j
public class AuthController {
    @Autowired
    private Util util;
    @Autowired
    protected TokenUtil tokenUtil;
    @Autowired
    private UserService userService;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private RedisService redisService;
    @PostMapping("/authenticate")
    public AuthResp authenticate(@RequestBody AuthReq req) {
        User user = userService.getLogin(req.getUsername(), req.getPassword(), true);
        List<Profile> aProfiles = profileService.getByUserId(user.getId());
        if (aProfiles.isEmpty()) {
            throw new NoProfileException();
        }
        List<ProfileShow> profiles = util.profileToShow(aProfiles, true);
        ProfileShow bindProfile = profiles.get(0);
        String cToken = req.getClientToken() == null ? TokenUtil.genClientToken() : req.getClientToken();
        String aToken = tokenUtil.genAccessToken(user, cToken, bindProfile.getId());
        redisService.saveToken(aToken, user.getId());
        log.info("New login came: " + user.getUsername());
        return new AuthResp()
                .setAccessToken(aToken)
                .setClientToken(cToken)
                .setAvailableProfiles(profiles)
                .setSelectedProfile(bindProfile)
                .setUser(req.getRequestUser() ? util.userToShow(user) : null);
    }
    @PostMapping("/refresh")
    public AuthResp refresh(@RequestBody RefreshReq req) {
        AToken aToken = tokenUtil.verifyAccessToken(req.getAccessToken(), req.getClientToken(), false);
        if (aToken == null) {
            throw new InvalidTokenException();
        }
        User user = userService.getLoginNoPwd(aToken.name());
        if (user == null) {
            throw new InvalidTokenException();
        }
        Profile bindProfile;
        if (req.getSelectedProfile() != null) {
            bindProfile = profileService.getById(req.getSelectedProfile().getId());
            if (bindProfile == null || !bindProfile.getBindUser().equals(user.getId())) {
                throw new ProfileMismatchException();
            }
        } else {
            bindProfile = profileService.getById(aToken.bindProfile());
            if (bindProfile == null) {
                //Selected profile disappeared and need token update
                throw new InvalidTokenException();
            }
        }
        String cToken = req.getClientToken() == null ? TokenUtil.genClientToken() : req.getClientToken();
        String newAToken = tokenUtil.genAccessToken(user, cToken, bindProfile.getId());
        redisService.removeToken(req.getAccessToken());
        redisService.saveToken(newAToken, user.getId());
        log.info("New token refresh: " + user.getUsername());
        return new AuthResp()
                .setAccessToken(newAToken)
                .setClientToken(cToken)
                .setAvailableProfiles(null)
                .setSelectedProfile(util.profileToShow(bindProfile, true))
                .setUser(req.isRequestUser() ? util.userToShow(user) : null);
    }
    @PostMapping("/validate")
    public ResponseEntity<?> validate(@RequestBody TokenGReq req) {
        tokenUtil.verifyAccessToken(req.getAccessToken(), req.getClientToken(), true);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PostMapping("/invalidate")
    public ResponseEntity<?> invalidate(@RequestBody TokenGReq req) {
        redisService.removeToken(req.getAccessToken());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PostMapping("/signout")
    public ResponseEntity<?> signout(@RequestBody AuthReq req) {
        User user = userService.getLogin(req.getUsername(), req.getPassword());
        redisService.clearToken(user.getId());
        log.info("New logout: " + user.getUsername());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}