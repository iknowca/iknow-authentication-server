package xyz.iknow.authenticaionserver.utility.redis.token;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import xyz.iknow.authenticaionserver.utility.redis.token.Token;

import java.util.Optional;

@Repository
public interface TokenRepository extends CrudRepository<Token, Long>{
    Optional<Token> findByJwt(String jwt);
    Optional<Token> findById(Long id);
}
