package xyz.iknow.authenticaionserver.utility.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class JwtUtility {
    @Value("${jwt.secret}")
    private String secret;


    public String generateToken(Map<String, Object> valueMap, long expiration) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        Map<String, Object> payloads = new HashMap<>();
        payloads.putAll(valueMap);

        String jwtStr = Jwts.builder()
                .setHeader(headers)
                .setClaims(payloads)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(Date.from(ZonedDateTime.now().plusSeconds(expiration).toInstant()))
                .signWith(io.jsonwebtoken.SignatureAlgorithm.HS256, secret)
                .compact();
        return jwtStr;
    }

    public Map<String, Object> parseToken(String token) {
        Map<String, Object> claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }
}
