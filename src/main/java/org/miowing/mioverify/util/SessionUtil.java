package org.miowing.mioverify.util;

import org.miowing.mioverify.exception.InvalidSessionException;
import org.miowing.mioverify.exception.InvalidTokenException;
import org.miowing.mioverify.exception.UserMismatchException;
import org.miowing.mioverify.pojo.AToken;
import org.miowing.mioverify.pojo.Profile;
import org.miowing.mioverify.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Operations of sessions.
 */
@Component
public class SessionUtil {
    public static final String SESSION_PREF = "ss_";
    @Autowired
    private TokenUtil tokenUtil;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private ProfileService profileService;
    public Profile verifySession(String serverId, String username) {
        if (Boolean.FALSE.equals(redisTemplate.hasKey(SESSION_PREF + serverId))) {
            throw new InvalidSessionException();
        }
        String token = redisTemplate.opsForValue().get(SESSION_PREF + serverId);
        AToken aToken = tokenUtil.verifyAccessToken(token, null, true);
        if (aToken == null) {
            throw new InvalidTokenException();
        }
        if (!username.equals(aToken.name())) {
            throw new UserMismatchException();
        }
        return profileService.getById(aToken.bindProfile());
    }
}