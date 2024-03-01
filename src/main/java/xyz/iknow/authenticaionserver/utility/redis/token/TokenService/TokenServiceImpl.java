package xyz.iknow.authenticaionserver.utility.redis.token.TokenService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xyz.iknow.authenticaionserver.utility.redis.token.TokenRepository.AccessTokenRepository;
import xyz.iknow.authenticaionserver.utility.redis.token.TokenRepository.RefreshTokenRepository;
import xyz.iknow.authenticaionserver.utility.redis.token.token.AccessToken;
import xyz.iknow.authenticaionserver.utility.redis.token.token.RefreshToken;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final AccessTokenRepository accessTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public AccessToken save(AccessToken token) {
        return accessTokenRepository.save(token);
    }

    @Override
    public RefreshToken save(RefreshToken token) {
        return refreshTokenRepository.save(token);
    }

    @Override
    public Optional<AccessToken> findAccessTokenById(Long id) {
        return accessTokenRepository.findById(id);
    }

    @Override
    public Optional<RefreshToken> findRefreshTokenById(Long id) {
        return refreshTokenRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        accessTokenRepository.deleteById(id);
        refreshTokenRepository.deleteById(id);
    }
}
