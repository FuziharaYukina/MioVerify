package org.miowing.mioverify.pojo.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.lang.Nullable;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrResp {
    private String error;
    private String errorMessage;
    private @Nullable String cause;
}