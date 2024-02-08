package xyz.iknow.authenticaionserver.security.jwt.service;

import xyz.iknow.authenticaionserver.domain.account.entity.Account;

import java.util.Map;

public interface JwtService {
    String generateAccessToken(Account account);

    String generateRefreshToken(Account account);

    Map<String, Object> parseToken(String token);
}
