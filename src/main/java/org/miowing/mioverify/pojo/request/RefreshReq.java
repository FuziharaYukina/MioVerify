package org.miowing.mioverify.pojo.request;

import lombok.Data;
import org.miowing.mioverify.pojo.ProfileShow;
import org.springframework.lang.Nullable;

@Data
public class RefreshReq {
    private String accessToken;
    private @Nullable String clientToken;
    private boolean requestUser = false;
    private @Nullable ProfileShow selectedProfile;
}