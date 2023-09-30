package org.miowing.mioverify.pojo.request;

import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class TokenGReq {
    private String accessToken;
    private @Nullable String clientToken;
}