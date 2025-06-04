package com.melvstein.solar_system.config;

import com.melvstein.solar_system.util.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ConditionalOnProperty(name = "redis.cache.enabled", havingValue = "false", matchIfMissing = true)
public class NoCacheConfig {
    public NoCacheConfig() {
        log.info("{} - No cache config", Utils.currentMethod());
    }

    @Bean
    public CacheManager cacheManager() {
        return new NoOpCacheManager();
    }
}
