package xyz.iknow.authenticaionserver.utility.redis.token.TokenRepository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import xyz.iknow.authenticaionserver.utility.redis.token.token.AccessToken;

import java.util.Optional;

@Repository
public interface AccessTokenRepository extends CrudRepository<AccessToken, Long> {
    Optional<AccessToken> findById(Long id);
}
