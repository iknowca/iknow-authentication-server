package xyz.iknow.authenticaionserver.domain.jwt.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("JwtService 테스트")
class JwtServiceTest {

    @Autowired
    private JwtService jwtService;
    @Test
    @DisplayName("토큰 생성 및 파싱 테스트")
    public void testGenerateToken() {
        //given
        Long id = 1L;
        //when
        String accessToken = jwtService.generateAccessToken(id);
        String refreshToken = jwtService.generateRefreshToken(id);

        Map accessTokenValues = jwtService.parseToken(accessToken);
        Map refreshTokenValues = jwtService.parseToken(refreshToken);
        //then
        assertThat( (Integer) accessTokenValues.get("accountId")).isEqualTo(id.intValue());
        assertThat( refreshTokenValues.get("accountId")).isEqualTo(id.intValue());
    }
}