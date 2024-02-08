package xyz.iknow.authenticaionserver.domain.jwt.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.iknow.authenticaionserver.domain.account.entity.Account;
import xyz.iknow.authenticaionserver.security.jwt.service.JwtService;
import xyz.iknow.authenticaionserver.utility.jwt.JwtUtility;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("JwtService 테스트")
class JwtServiceTest {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private JwtUtility jwtUtility;
    @Test
    @DisplayName("토큰 생성 및 파싱 테스트")
    public void testGenerateToken() {
        //given
        Long id = 1L;
        String email = "validEmail@test.com";
        Account account = Account.builder().id(id).email(email).build();
        //when
        String accessToken = jwtService.generateAccessToken(account);
        String refreshToken = jwtService.generateRefreshToken(account);

        Map accessTokenValues = jwtUtility.parseToken(accessToken);
        Map refreshTokenValues = jwtUtility.parseToken(refreshToken);
        //then
        assertThat( (Integer) accessTokenValues.get("accountId")).isEqualTo(id.intValue());
        assertThat( refreshTokenValues.get("accountId")).isEqualTo(id.intValue());
    }
}