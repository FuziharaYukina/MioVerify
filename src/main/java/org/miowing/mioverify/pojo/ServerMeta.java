package org.miowing.mioverify.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Component
@ConfigurationProperties(prefix = "mioverify.props")
@ConfigurationPropertiesBinding
public class ServerMeta {
    private Meta meta;
    private List<String> skinDomains;
    private String signaturePublicKey;
    @Data
    @Accessors(chain = true)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class Meta {
        private String serverName;
        private String implementationName;
        private String implementationVersion;
        private Links links;
        @JsonProperty("feature.non_email_login")
        private Boolean featureNonEmailLogin;
        @JsonProperty("feature.legacy_skin_api")
        private Boolean featureLegacySkinApi = false;
        @JsonProperty("feature.no_mojang_namespace")
        private Boolean featureNoMojangNamespace;
        @JsonProperty("feature.enable_mojang_anti_features")
        private Boolean featureEnableMojangAntiFeature;
        @JsonProperty("feature.enable_profile_key")
        private Boolean featureEnableProfileKey;
        @JsonProperty("feature.username_check")
        private Boolean featureUsernameCheck;
        @Data
        @Accessors(chain = true)
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        public static class Links {
            private String homepage;
            private String register;
        }
    }
}