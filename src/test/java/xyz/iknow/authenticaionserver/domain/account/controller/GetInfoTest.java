package xyz.iknow.authenticaionserver.domain.account.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import xyz.iknow.authenticaionserver.domain.account.entity.LocalAccount;
import xyz.iknow.authenticaionserver.test.IntegrationTest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("내 정보 조회 테스트")
public class GetInfoTest extends IntegrationTest {
    @Nested
    @DisplayName("클라이언트가 내 정보를 조회할 때")
    class Describe_requestGetInfo {
        String accessToken;
        String email;
        String password;
        Long id;

        @BeforeEach
        void setUp() {
            email = ag.getTestEmail();
            password = ag.getTestPassword();
            LocalAccount account = LocalAccount.builder()
                    .email(email)
                    .password(passwordEncoder.encode(ag.getTestPassword()))
                    .build();
            accountRepository.save(account);
            id = account.getId();

            accessToken = jwtService.generateAccessToken(account);
        }
        @Nested
        @DisplayName("토큰이 유효한 경우")
        class Context_tokenIsValid {
            @Test
            @DisplayName("내 정보를 반환한다.")
            void it_returns_myInfo() throws Exception {
                ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/account")
                        .header("Authorization", "Bearer " + accessToken));

                result.andExpect(status().isOk());

                result.andExpect(jsonPath("id").value(id));
            }
        }

        @Nested
        @DisplayName("토큰이 유효하지 않은 경우")
        class Context_tokenIsNotValid {
            String accessToken = "invalidToken";
            @Test
            @DisplayName("401 에러를 반환한다.")
            void it_returns_401Error() throws Exception {
                ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/account")
                        .header("Authorization", "Bearer " + accessToken));

                result.andExpect(status().isUnauthorized());
            }
        }
    }
}
