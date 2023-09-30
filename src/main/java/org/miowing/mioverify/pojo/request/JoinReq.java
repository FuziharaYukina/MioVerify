package org.miowing.mioverify.pojo.request;

import lombok.Data;

@Data
public class JoinReq {
    private String accessToken;
    private String selectedProfile;
    private String serverId;
}