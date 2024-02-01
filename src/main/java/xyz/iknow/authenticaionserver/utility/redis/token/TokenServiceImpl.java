package xyz.iknow.authenticaionserver.utility.redis.token;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService{

    private final TokenRepository tokenRepository;

    public Token save(Token token) {
        return tokenRepository.save(token);
    }

    public Token findById(Long id) {
        return tokenRepository.findById(id).orElse(null);
    }
}
