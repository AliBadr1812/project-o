package com.ali_b1812.app.util;

import com.ali_b1812.app.config.JwtProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtUtil {

    private final JwtProperties jwtProperties;

    /** Build a 32-byte HMAC-SHA-256 key from the configured passphrase. */
    private SecretKey signingKey() {
        byte[] src  = jwtProperties.getPassphrase().getBytes(StandardCharsets.UTF_8);
        byte[] key  = new byte[32];
        System.arraycopy(src, 0, key, 0, Math.min(src.length, 32));
        return Keys.hmacShaKeyFor(key);
    }

    /** Generate a signed JWT for the given user. */
    public String generateToken(UserDetails userDetails) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .subject(userDetails.getUsername())        // stores the email
                .issuer(jwtProperties.getIssuer())
                .issuedAt(new Date(now))
                .expiration(new Date(now + jwtProperties.getExpiration() * 1_000L))
                .signWith(signingKey())
                .compact();
    }

    /** Extract the subject (email) from a token without validating expiry. */
    public String getEmailFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    /** Return true iff the token signature is valid, not expired, and belongs to userDetails. */
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            String subject = getEmailFromToken(token);
            return subject.equals(userDetails.getUsername())
                    && !parseClaims(token).getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException ex) {
            log.debug("JWT validation failed: {}", ex.getMessage());
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
