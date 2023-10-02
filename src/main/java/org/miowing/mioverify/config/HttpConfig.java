package org.miowing.mioverify.config;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.miowing.mioverify.util.DataUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpConfig {
    @Autowired
    private DataUtil dataUtil;
    @Bean
    public TomcatServletWebServerFactory servletWebServerFactory(Connector connector) {
        if (!dataUtil.isSslEnabled()) {
            return new TomcatServletWebServerFactory();
        }
        TomcatServletWebServerFactory serverFactory = new TomcatServletWebServerFactory() {
            @Override
            protected void postProcessContext(Context context) {
                SecurityConstraint securityConstraint = new SecurityConstraint();
                securityConstraint.setUserConstraint("CONFIDENTIAL");
                SecurityCollection collection = new SecurityCollection();
                collection.addPattern("/*");
                securityConstraint.addCollection(collection);
                context.addConstraint(securityConstraint);
            }
        };
        serverFactory.addAdditionalTomcatConnectors(connector);
        return serverFactory;
    }
    @Bean
    public Connector createHttpConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setScheme("http");
        connector.setPort(dataUtil.getHttpPort());
        connector.setSecure(false);
        connector.setRedirectPort(dataUtil.getPort());
        return connector;
    }
}