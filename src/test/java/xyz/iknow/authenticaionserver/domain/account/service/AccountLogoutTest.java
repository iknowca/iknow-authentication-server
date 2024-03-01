package xyz.iknow.authenticaionserver.domain.account.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import xyz.iknow.authenticaionserver.domain.account.dto.LocalAccountDTO;
import xyz.iknow.authenticaionserver.domain.account.entity.Account;
import xyz.iknow.authenticaionserver.test.UnitTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@DisplayName("AcccountService.logout Test")
public class AccountLogoutTest extends UnitTest {
    Account account;

    @BeforeEach
    void setup() throws Exception {
        String email = ag.getTestEmail();
        String password = ag.getTestPassword();
        mockMvc.perform(post("/account/join")
                .content(objectMapper.writeValueAsString(LocalAccountDTO.builder()
                        .email(email)
                        .password(password)
                        .build()))
                .contentType("application/json"));
        account = accountRepository.findByEmail(email).get();
    }

    @Nested
    @DisplayName("클라이언트가 로그아웃을 요청할 때")
    class Describe_whenClientRequestLogout {
        @Nested
        @DisplayName("로그아웃에 성공했다면")
        class Context_logoutSuccess {
            @BeforeEach
            void setup() {
                jwtService.generateAccessToken(account);
                jwtService.generateRefreshToken(account);
            }

            @Test
            @DisplayName("redis에서 token이 삭제된다.")
            void it_returns200ok() {
                accountService.logout(account);
                assertThat(accessTokenRepository.findById(account.getId()).isPresent()).isFalse();
                assertThat(refreshTokenRepository.findById(account.getId()).isPresent()).isFalse();
            }

        }
    }
}
