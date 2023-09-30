package org.miowing.mioverify.controller;

import lombok.extern.slf4j.Slf4j;
import org.miowing.mioverify.exception.InvalidTokenException;
import org.miowing.mioverify.exception.ProfileMismatchException;
import org.miowing.mioverify.exception.ProfileNotFoundException;
import org.miowing.mioverify.pojo.AToken;
import org.miowing.mioverify.pojo.Profile;
import org.miowing.mioverify.pojo.ProfileShow;
import org.miowing.mioverify.pojo.request.JoinReq;
import org.miowing.mioverify.service.ProfileService;
import org.miowing.mioverify.service.RedisService;
import org.miowing.mioverify.util.SessionUtil;
import org.miowing.mioverify.util.TokenUtil;
import org.miowing.mioverify.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sessionserver/session/minecraft")
@Slf4j
public class SessionController {
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private SessionUtil sessionUtil;
    @Autowired
    private Util util;
    @Autowired
    private RedisService redisService;
    @Autowired
    private ProfileService profileService;
    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody JoinReq req) {
        AToken aToken = tokenUtil.verifyAccessToken(req.getAccessToken(), null, true);
        if (aToken == null) {
            throw new InvalidTokenException();
        }
        if (!aToken.bindProfile().equals(req.getSelectedProfile())) {
            throw new ProfileMismatchException();
        }
        redisService.saveSession(req.getServerId(), req.getAccessToken());
        log.info("New player joined: (profileId) " + aToken.bindProfile());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/hasJoined")
    public ProfileShow hasJoined(String username, String serverId) {
        return util.profileToShow(sessionUtil.verifySession(serverId, username), true);
    }
    @GetMapping("/profile/{uuid}")
    public ProfileShow profile(@PathVariable String uuid, @RequestParam(defaultValue = "true") boolean unsigned) {
        Profile p = profileService.getById(uuid);
        if (p == null) {
            throw new ProfileNotFoundException();
        }
        return util.profileToShow(p, !unsigned);
    }
}