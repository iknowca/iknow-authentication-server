package xyz.iknow.authenticaionserver.domain.account.controller;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import xyz.iknow.authenticaionserver.domain.account.entity.LocalAccount;
import xyz.iknow.authenticaionserver.test.IntegrationTest;

@DisplayName("토큰 갱신 테스트")
public class TokenRefreshTest extends IntegrationTest {

    @Nested
    @DisplayName("클라이언트가 토큰을 갱신 요청할 때")
    class Describe_requestTokenRefresh {

        String email;
        String password;
        String refreshToken;

        @BeforeEach
        void setUp() {
            email = ag.getTestEmail();
            password = ag.getTestPassword();
            LocalAccount account = LocalAccount.builder().email(email).password(passwordEncoder.encode(ag.getTestPassword())).build();
            accountRepository.save(account);

            String refreshToken = jwtService.generateRefreshToken(account);
        }

        @Nested
        @DisplayName("리프레시 토큰이 유효한 경우")
        class Context_refreshTokenIsValid {
            @Nested
            @DisplayName("리프레시 토큰이 만료되지 않은 경우")
            class Context_refreshTokenIsNotExpired {
                @Test
                @DisplayName("새로운 액세스 토큰을 반환한다")
                void it_returns_newAccessToken() throws Exception {
                    ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/jwt/refresh").cookie(new Cookie("refreshToken", refreshToken)));

                }
            }

        }

        @Nested
        @DisplayName("토큰이 유효하지 않은 경우")
        class Context_tokenIsNotValid {

        }
    }
}
