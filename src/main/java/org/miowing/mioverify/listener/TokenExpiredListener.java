package org.miowing.mioverify.listener;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.miowing.mioverify.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;

/**
 * Listener of expired tokens.
 * There are pairs of tokens (Token, Marker, User).
 * The Marker will be auto-removed by redis, and the remains will be deleted here when it occurs.
 * The offline-server problem will be resolved by another class.
 */
@Slf4j
@Component
public class TokenExpiredListener extends KeyExpirationEventMessageListener {
    @Autowired
    private StringRedisTemplate redisTemplate;
    public TokenExpiredListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("Tokens expired Listener hooked.");
        super.afterPropertiesSet();
    }
    @Override
    protected void doHandleMessage(Message message) {
        String key = new String(message.getBody(), StandardCharsets.UTF_8);
        if (key.startsWith(TokenUtil.TMARK_PREF)) {
            String cKey = StrUtil.subSuf(key, 3); //token
            String tKey = TokenUtil.TOKEN_PREF + cKey; //at_token
            String userId = redisTemplate.opsForValue().get(tKey); //id
            String tUserId = TokenUtil.USERID_PREF + userId; //tv_id
            redisTemplate.delete(tKey);
            HashOperations<String, Object, Object> hops = redisTemplate.opsForHash();
            if (hops.size(tUserId) < 2) {
                redisTemplate.delete(tUserId);
            } else {
                hops.delete(tUserId, cKey);
            }
        }
        super.doHandleMessage(message);
    }
}