package xyz.iknow.authenticaionserver.utility.redis.token.TokenService;

import xyz.iknow.authenticaionserver.utility.redis.token.token.AccessToken;
import xyz.iknow.authenticaionserver.utility.redis.token.token.RefreshToken;

import java.util.Optional;

public interface TokenService {
    AccessToken save(AccessToken token);

    RefreshToken save(RefreshToken token);

    Optional<AccessToken> findAccessTokenById(Long id);

    Optional<RefreshToken> findRefreshTokenById(Long id);

    void delete(Long id);
}
