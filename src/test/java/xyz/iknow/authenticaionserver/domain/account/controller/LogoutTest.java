package xyz.iknow.authenticaionserver.domain.account.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import xyz.iknow.authenticaionserver.domain.account.entity.LocalAccount;
import xyz.iknow.authenticaionserver.test.IntegrationTest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("로그아웃 테스트")
public class LogoutTest extends IntegrationTest {
    @Nested
    @DisplayName("로그아웃 요청이 들어왔을 때")
    class Describe_logout {

        String accessToken;

        @BeforeEach
        void setup() {
            LocalAccount account = LocalAccount.builder()
                    .email(ag.getTestEmail())
                    .password(ag.getTestPassword())
                    .build();
            accountRepository.save(account);
            accessToken = jwtService.generateAccessToken(account);
        }

        @Nested
        @DisplayName("로그아웃에 성공하면")
        class Context_logout_success {
            @Test
            @DisplayName("200 OK를 반환한다")
            void it_returns_200ok() throws Exception {
                ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/account/logout")
                        .header("Authorization", "Bearer " + accessToken));

                result.andExpect(status().isOk());
            }
        }

        @Nested
        @DisplayName("accessToken이 적절하지 않아 로그아웃에 실패하면")
        class Context_logout_fail {
            String accessToken = "invalid token";
            @Test
            @DisplayName("401 Unauthorized를 반환한다")
            void it_returns_bad_request() throws Exception {
                ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/account/logout")
                        .header("Authorization", "Bearer " + accessToken));

                result.andExpect(status().isUnauthorized());
            }
        }
    }
}
