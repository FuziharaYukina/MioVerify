package org.miowing.mioverify.config;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.miowing.mioverify.service.RedisService;
import org.miowing.mioverify.util.TokenUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import javax.sql.DataSource;
import java.util.Set;

@Configuration
@Slf4j
public class DataInitializer implements InitializingBean {
    @Value("classpath:db/schema.sql")
    private Resource sqlSchema;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RedisService redisService;
    @Bean
    public DataSourceInitializer dataSourceInitializer(final DataSource dataSource) {
        log.info("Initializing data tables...");
        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
        dataSourceInitializer.setDataSource(dataSource);
        dataSourceInitializer.setDatabasePopulator(databasePopulator());
        return dataSourceInitializer;
    }
    private DatabasePopulator databasePopulator() {
        ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
        resourceDatabasePopulator.addScript(sqlSchema);
        return resourceDatabasePopulator;
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("Getting all tokens in redis...");
        Set<String> keys = redisTemplate.keys(TokenUtil.TOKEN_PREF + "*");
        if (keys == null) {
            log.info("No tokens found.");
            return ;
        }
        log.info("Found " + keys.size() + ". Start checking validation...");
        int count = 0;
        for (String key : keys)  {
            String t = StrUtil.subSuf(key, 3);
            Boolean hasKey = redisTemplate.hasKey(TokenUtil.TMARK_PREF + t);
            if (hasKey == null || !hasKey) {
                redisService.removeToken(t);
                count++;
            }
        }
        if (count > 0) {
            log.info("Cleared " + count + " token(s) expired.");
        }
    }
}