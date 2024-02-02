package xyz.iknow.authenticaionserver.domain.jwt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xyz.iknow.authenticaionserver.utility.jwt.JwtUtility;
import xyz.iknow.authenticaionserver.utility.redis.token.Token;
import xyz.iknow.authenticaionserver.utility.redis.token.TokenService;

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
    public String generateAccessToken(Long accountId) {
        Map<String, Object> valueMap = Map.of("accountId", accountId);
        String accessToken = jwtUtility.generateToken(valueMap, accessTokenExpiration);
        tokenService.save(new Token(accountId, Token.TokenType.ACCESS, accessToken, accessTokenExpiration));
        return accessToken;
    }
    @Override
    public String generateRefreshToken(Long accountId) {
        Map<String, Object> valueMap = Map.of("accountId", accountId);
        String refreshToken = jwtUtility.generateToken(valueMap, refreshTokenExpiration);
        tokenService.save(new Token(accountId, Token.TokenType.REFRESH, refreshToken, refreshTokenExpiration));
        return refreshToken;
    }
    @Override
    public Map<String, Object> parseToken(String token) {
        return jwtUtility.parseToken(token);
    }
}
