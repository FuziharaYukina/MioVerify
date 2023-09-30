package org.miowing.mioverify.pojo.request;

import lombok.Data;

@Data
public class AuthReq {
    private String username;
    private String password;
    private String clientToken;
    private Boolean requestUser = false;
    //ignored: agent: {}
}