package xyz.iknow.authenticaionserver.utility.redis.token;

public interface TokenService {
    Token save(Token token);
    Token findById(Long id);
}
