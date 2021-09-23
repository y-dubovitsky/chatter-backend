package ru.ydubovitsky.chatter.security;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.ydubovitsky.chatter.entity.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider { // Этот класс отвечает за создание Токена

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenProvider.class);

    public String generateWebToken(Authentication authentication) { //FIXME Что такое Authentication? Как я помню, это данные о аутентифицированном пользователе
        User user = (User) authentication.getPrincipal();
        Date now = new Date(System.currentTimeMillis());
        Date dateExpiration = new Date(now.getTime() + SecurityConstants.EXPIRATION_TIME);
        String userId = Long.toString(user.getId());

        Map<String, Object> claims = new HashMap<>(); // Объект, из которого будет генерироваться токен

        claims.put("id", userId);
        claims.put("username", user.getUsername());
        claims.put("lastname", user.getLastname());
        claims.put("email", user.getEmail());

        return Jwts.builder() // Генерируем ТОКЕН
                .setSubject(userId)
                .addClaims(claims)
                .setIssuedAt(now)
                .setExpiration(dateExpiration)
                // https://stackoverflow.com/questions/50201237/base64-encoded-key-bytes-may-only-be-specified-for-hmac-signatures
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.SECRET)
                .compact();
    }

    public boolean validateToken(String token) { // Валидируем токен!
        try {
            Jwts.parser()
                    .setSigningKey(SecurityConstants.SECRET)
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException | MalformedJwtException | ExpiredJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            LOGGER.error(String.valueOf(e));
            return false;
        }
    }

    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SecurityConstants.SECRET)
                .parseClaimsJws(token)
                .getBody();

        String userId = (String) claims.get("id");
        return Long.parseLong(userId);
    }
}
