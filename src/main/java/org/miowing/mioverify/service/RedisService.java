package org.miowing.mioverify.service;

public interface RedisService {
    void saveToken(String token, String userId);
    boolean checkToken(String token);
    void removeToken(String token);
    void clearToken(String userId);
    void saveSession(String serverId, String token);
}