package com.todo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 在这里简化RSA的使用，将公钥和私钥写入到配置文件中，不再生成新的公钥和私钥
 */
@Configuration
@PropertySource("classpath:rsa.properties")
@ConfigurationProperties(prefix = "rsa")
@Data
public class RSAConfig {

    private String privateStr;
    private String publishStr;

}