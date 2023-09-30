package org.miowing.mioverify.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.lang.Nullable;

import java.util.List;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ProfileShow {
    private String id;
    private String name;
    private List<Property> properties;
    @Data
    @Accessors(chain = true)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public static class Property {
        private String name;
        private String value;
        private @Nullable String signature;
    }
}