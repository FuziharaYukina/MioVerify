package org.miowing.mioverify.pojo.request;

import lombok.Data;

@Data
public class UserRegisterReq {
    private String username;
    private String password;
    private String preferredLang = "zh_CN";
    private String key;
}