package xyz.iknow.authenticaionserver.utility.redis;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.iknow.authenticaionserver.utility.redis.token.Token;
import xyz.iknow.authenticaionserver.utility.redis.token.TokenRepository;
import xyz.iknow.authenticaionserver.utility.redis.token.TokenService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@DisplayName("Redis 테스트")
public class RedisTest {
    @Autowired
    private TokenService tokenService;

    @Test
    @DisplayName("토큰 저장 및 읽기 성공")
    public void testSaveAndFind() {
        //given
        Token token = Token.builder().id(1L).type(Token.TokenType.ACCESS).jwt("test").expiration(3000L).build();

        //when
        tokenService.save(token);
        Token savedToken = tokenService.findById(1L);

        //then
        assertThat(savedToken).isEqualTo(token);
    }
    @Test
    @DisplayName("토큰 저장 성공 및 읽기 실패")
    public void testSaveAndFind2() {
        //given
        Token token = Token.builder().id(2L).type(Token.TokenType.ACCESS).jwt("test").expiration(3000L).build();

        //when
        tokenService.save(token);
        Token savedToken = tokenService.findById(-1L);

        //then
        assertThat(savedToken).isNull();
    }
}
