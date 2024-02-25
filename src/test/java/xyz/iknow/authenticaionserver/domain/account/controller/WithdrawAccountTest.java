package xyz.iknow.authenticaionserver.domain.account.controller;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import xyz.iknow.authenticaionserver.test.IntegrationTest;

import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("회원 탈퇴 테스트")
public class WithdrawAccountTest extends IntegrationTest {
    @Nested
    @DisplayName("클라이언트가 회원 탈퇴를 요청할 때")
    class Describe_withdrawAccount {
        String email;
        String password;
        String accessToken;

        @BeforeEach
        void setUp() throws Exception {
            email = ag.getTestEmail();
            password = ag.getTestPassword();

            String responseContent;

            mockMvc.perform(MockMvcRequestBuilders.post("/account/join")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(Map.of("email", email, "password", password))));
            mockMvc.perform(MockMvcRequestBuilders.post("/account/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(Map.of("email", email, "password", password))))
                    .andDo(result -> {
                        JsonNode jsonNode = objectMapper.readTree(result.getResponse().getContentAsString());
                        accessToken = jsonNode.get("accessToken").asText();
                    });
        }

        @Nested
        @DisplayName("정상적인 요청이면")
        class Context_WhenRequestIsValid {

            @Test
            @DisplayName("회원 탈퇴에 성공한다")
            void It_success_withdrawAccount() throws Exception {
                ResultActions result = mockMvc.perform(MockMvcRequestBuilders.delete("/account")
                        .header("Authorization", accessToken));
                result.andExpect(status().isOk());
            }
            @Test
            @DisplayName("회원 탈퇴 후 로그인을 시도하면 401 Unauthorized를 응답한다")
            void It_responds_401Unauthorized() throws Exception {
                mockMvc.perform(MockMvcRequestBuilders.delete("/account")
                        .header("Authorization", accessToken));
                mockMvc.perform(MockMvcRequestBuilders.post("/account/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("email", email, "password", password))))
                        .andExpect(status().isUnauthorized());
            }
        }

        @Nested
        @DisplayName("클라이언트가 로그인하지 않았을 때")
        class Context_WhenClientIsNotLoggedIn {

            @Test
            @DisplayName("401 Unauthorized를 응답한다")
            void It_responds_401Unauthorized() throws Exception {
                mockMvc.perform(MockMvcRequestBuilders.delete("/account"))
                        .andExpect(status().isUnauthorized());
            }
        }
    }
}
