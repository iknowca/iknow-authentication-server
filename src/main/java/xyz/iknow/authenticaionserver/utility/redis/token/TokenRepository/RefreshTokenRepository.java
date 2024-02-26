package xyz.iknow.authenticaionserver.utility.redis.token.TokenRepository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import xyz.iknow.authenticaionserver.utility.redis.token.token.RefreshToken;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long>{
    Optional<RefreshToken> findById(Long id);
}
