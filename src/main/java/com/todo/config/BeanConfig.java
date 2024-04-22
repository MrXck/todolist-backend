package com.todo.config;

import com.todo.utils.CacheUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

    @Bean
    public CacheUtils<String, String> expiringKeyUtils() {
        return new CacheUtils<>();
    }
}
