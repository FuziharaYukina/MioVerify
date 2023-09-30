package org.miowing.mioverify.util;

import cn.hutool.core.util.IdUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.miowing.mioverify.pojo.AToken;
import org.miowing.mioverify.pojo.User;
import org.miowing.mioverify.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import java.util.Date;

/**
 * Operations of tokens.
 */
@Component
public class TokenUtil {
    @Autowired
    private DataUtil dataUtil;
    @Autowired
    private RedisService redisService;
    public static String TOKEN_PREF = "tk_";
    public static String TMARK_PREF = "tm_";
    public static String USERID_PREF = "tv_";
    public static String genClientToken() {
        return IdUtil.simpleUUID();
    }
    public String genAccessToken(User user, @Nullable String clientToken, String bindProfile) {
        Date invalidAt = new Date(System.currentTimeMillis() + dataUtil.getTokenInvalid().toMillis());
        return JWT.create()
                .withClaim("wexp", System.currentTimeMillis() + dataUtil.getTokenExpire().toMillis())
                .withClaim("ct", clientToken == null ? genClientToken() : clientToken)
                .withClaim("name", user.getUsername())
                .withClaim("bindp", bindProfile)
                .withExpiresAt(invalidAt)
                .sign(Algorithm.HMAC256(dataUtil.getTokenSign()));
    }
    public @Nullable AToken verifyAccessToken(String accessToken, @Nullable String clientToken, boolean strictExpire) {
        try {
            DecodedJWT dJWT = JWT
                    .require(Algorithm.HMAC256(dataUtil.getTokenSign()))
                    .build()
                    .verify(accessToken);
            String cToken = dJWT.getClaim("ct").asString();
            //check clientToken
            if (clientToken != null && !cToken.equals(clientToken)) {
                return null;
            }
            //check weak expires
            if (strictExpire) {
                long expiresAt = dJWT.getClaim("wexp").asLong();
                if (System.currentTimeMillis() > expiresAt) {
                    return null;
                }
            }
            //If LOGOUT, INVALIDATE or REFRESH occurs (or expires), the token won't be in the redis.
            if (!redisService.checkToken(accessToken)) {
                return null;
            }
            return new AToken(
                    cToken,
                    dJWT.getClaim("name").asString(),
                    dJWT.getClaim("bindp").asString()
            );
        } catch (JWTDecodeException | SignatureVerificationException | TokenExpiredException |
                 AlgorithmMismatchException e) {
            return null;
        }
    }
}