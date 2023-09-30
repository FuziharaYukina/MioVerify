package org.miowing.mioverify.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.lang.Nullable;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TexturesShow {
    private Long timestamp;
    private String profileId;
    private String profileName;
    private @Nullable Texture skin;
    private @Nullable Texture cape;
    @Data
    @Accessors(chain = true)
    public static class Texture {
        private String url;
        private Metadata metadata;
    }
    @Data
    @Accessors(chain = true)
    public static class Metadata {
        private String model;
    }
}