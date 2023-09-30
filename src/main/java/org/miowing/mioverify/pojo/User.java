package org.miowing.mioverify.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.lang.Nullable;

@Data
@Accessors(chain = true)
@TableName("users")
public class User {
    @TableId
    private String id;
    private String username;
    private String password;
    private @Nullable String preferredLang;
}