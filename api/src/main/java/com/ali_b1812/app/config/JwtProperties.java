package com.ali_b1812.app.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.jwt")
@Getter
@Setter
public class JwtProperties {
    /** HMAC-SHA-256 signing secret — must be at least 32 chars in production. */
    private String passphrase = "shopadmin-dev-secret-change-in-production";
    /** JWT issuer claim. */
    private String issuer = "shopadmin";
    /** Token lifetime in seconds. */
    private long expiration = 86400;
}
