package xyz.iknow.authenticaionserver.domain.jwt.service;

import java.util.Map;

public interface JwtService {
    String generateAccessToken(Long accountId);

    String generateRefreshToken(Long accountId);

    Map<String, Object> parseToken(String token);
}
