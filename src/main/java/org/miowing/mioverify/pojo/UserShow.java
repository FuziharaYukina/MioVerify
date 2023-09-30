package org.miowing.mioverify.pojo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.List;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserShow {
    private String id;
    private List<Property> properties;
    @Data
    @Accessors(chain = true)
    public static class Property {
        private String name;
        private String value;
    }
}