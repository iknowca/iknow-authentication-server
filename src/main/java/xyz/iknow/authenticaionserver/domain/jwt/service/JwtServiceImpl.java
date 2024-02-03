package xyz.iknow.authenticaionserver.domain.jwt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import xyz.iknow.authenticaionserver.domain.account.entity.Account;
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
    public String generateAccessToken(Account account) {
        Map<String, Object> valueMap = Map.of("accountId", account.getId(), "email", account.getEmail(), "nickname", (account.getNickname() != null)?account.getEmail():"");
        String accessToken = jwtUtility.generateToken(valueMap, accessTokenExpiration);
        Token token =
                Token.builder()
                        .id(account.getId())
                        .type(Token.TokenType.ACCESS)
                        .jwt(accessToken)
                        .expiration(accessTokenExpiration)
                        .build();
        tokenService.save(token);
        return accessToken;
    }
    @Override
    public String generateRefreshToken(Account account) {
        Map<String, Object> valueMap = Map.of("accountId", account.getId(), "email", account.getEmail(), "nickname", (account.getNickname() != null)?account.getEmail():"");
        String refreshToken = jwtUtility.generateToken(valueMap, refreshTokenExpiration);
        Token token =
                Token.builder()
                        .id(account.getId())
                        .type(Token.TokenType.REFRESH)
                        .jwt(refreshToken)
                        .expiration(accessTokenExpiration)
                        .build();
        tokenService.save(token);       return refreshToken;
    }
    @Override
    public Map<String, Object> parseToken(String token) {
        return jwtUtility.parseToken(token);
    }
}
