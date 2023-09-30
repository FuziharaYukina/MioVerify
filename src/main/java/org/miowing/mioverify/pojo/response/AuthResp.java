package org.miowing.mioverify.pojo.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;
import org.miowing.mioverify.pojo.ProfileShow;
import org.miowing.mioverify.pojo.UserShow;
import org.springframework.lang.Nullable;
import java.util.List;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AuthResp {
    private String accessToken;
    private String clientToken;
    private @Nullable List<ProfileShow> availableProfiles;
    private @Nullable ProfileShow selectedProfile;
    private @Nullable UserShow user;
}