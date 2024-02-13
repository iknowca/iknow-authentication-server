package xyz.iknow.authenticaionserver.security.jwt.service;

import io.jsonwebtoken.MalformedJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xyz.iknow.authenticaionserver.domain.account.entity.Account;
import xyz.iknow.authenticaionserver.security.jwt.exception.TokenException;
import xyz.iknow.authenticaionserver.utility.jwt.JwtUtility;
import xyz.iknow.authenticaionserver.utility.redis.token.TokenService.TokenService;
import xyz.iknow.authenticaionserver.utility.redis.token.token.AccessToken;
import xyz.iknow.authenticaionserver.utility.redis.token.token.RefreshToken;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService{
    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;
    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;
    private final JwtUtility jwtUtility;
    private final TokenService tokenService;
    @Override
    public String generateAccessToken(Account account) {
        Map<String, Object> valueMap = Map.of("accountId", account.getId());
        String accessToken = jwtUtility.generateToken(valueMap, accessTokenExpiration);
        AccessToken token = AccessToken.builder()
                .id(account.getId())
                .jwt(accessToken)
                .expiration(accessTokenExpiration)
                .build();
        tokenService.save(token);
        return accessToken;
    }
    @Override
    public String generateRefreshToken(Account account) {
        Map<String, Object> valueMap = Map.of("accountId", account.getId());
        String refreshToken = jwtUtility.generateToken(valueMap, refreshTokenExpiration);
        RefreshToken token = RefreshToken.builder()
                .id(account.getId())
                .jwt(refreshToken)
                .expiration(refreshTokenExpiration)
                .build();
        tokenService.save(token);
        return refreshToken;
    }
    @Override
    public Map<String, Object> parseToken(String token) {
        Map<String, Object> jwtValueMap;
        try {
            jwtValueMap = jwtUtility.parseToken(token);
        } catch (MalformedJwtException e) {
            throw new TokenException(TokenException.TOKEN_ERROR.MALFORMED_TOKEN);
        }

        Instant now = ZonedDateTime.now().toInstant();
        Instant exp = Instant.ofEpochSecond( Long.parseLong(jwtValueMap.get("exp").toString()));
        if(now.isAfter(exp)){
            throw new TokenException(TokenException.TOKEN_ERROR.EXPIRED_TOKEN);
        }
        return jwtValueMap;

    }
}
