package org.miowing.mioverify.util;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.nio.file.Path;
import java.time.Duration;

/**
 * All properties in application.yml will be injected to this.
 */
@Component
@Data
@Slf4j
public class DataUtil implements InitializingBean {
    @Value("${server.port}")
    private int port;
    @Value("${server.ssl.enabled}")
    private boolean sslEnabled;
    @Value("${server.ssl.http-port}")
    private int httpPort;
    @Value("${mioverify.texture.storage-loc}")
    private Path texturesPath;
    @Value("${mioverify.texture.default-skin-loc}")
    private Path defSkinLoc;
    @Value("${mioverify.props.use-https}")
    private boolean useHttps;
    @Value("${mioverify.props.server-domain}")
    private String serverDomain;
    @Value("${mioverify.security.sign-algorithm}")
    private String signAlgorithm;
    @Value("${mioverify.token.signature}")
    private String tokenSign;
    @Value("${mioverify.token.expire}")
    private Duration tokenExpire;
    @Value("${mioverify.token.invalid}")
    private Duration tokenInvalid;
    @Value("${mioverify.session.expire}")
    private Duration sessionExpire;
    @Value("${mioverify.security.profile-batch-limit}")
    private int profileBatchLimit;
    @Value("${mioverify.extern.register.enabled}")
    private boolean allowRegister;
    @Value("${mioverify.extern.register.allow-user}")
    private boolean allowRegUser;
    @Value("${mioverify.extern.register.allow-profile}")
    private boolean allowRegProfile;
    @Value("${mioverify.extern.register.permission-key.enabled}")
    private boolean usePermKey;
    @Value("${mioverify.extern.register.permission-key.key}")
    private String permKey;
    @Value("${mioverify.extern.register.profile-strict}")
    private boolean profileStrict;
    @Value("${mioverify.extern.multi-profile-name}")
    private boolean multiProfileName;
    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("Reading data from application file...");
    }
}