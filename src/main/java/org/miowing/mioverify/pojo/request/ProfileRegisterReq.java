package org.miowing.mioverify.pojo.request;

import lombok.Data;

@Data
public class ProfileRegisterReq {
    private String profileName;
    private String password;
    private String username;
    private boolean skinUploadAllow = true;
    private boolean capeUploadAllow = true;
    private String key;
}