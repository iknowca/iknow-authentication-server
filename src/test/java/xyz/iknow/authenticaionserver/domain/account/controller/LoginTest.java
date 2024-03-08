package xyz.iknow.authenticaionserver.domain.account.controller;

import org.junit.jupiter.api.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import xyz.iknow.authenticaionserver.domain.account.entity.AccountDetails;
import xyz.iknow.authenticaionserver.domain.account.entity.LocalAccount;
import xyz.iknow.authenticaionserver.test.IntegrationTest;

import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("로그인 테스트")
public class LoginTest extends IntegrationTest {
    @Nested
    @DisplayName("클라이언트가 로그인을 요청할때")
    class Describe_account_login {
        String email;
        String password;

        @BeforeEach
        void setUp() {
            email = ag.getTestEmail();
            password = ag.getTestPassword();

            LocalAccount account = LocalAccount.builder()
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .build();
            AccountDetails accountDetails = new AccountDetails();
            account.setAccountDetails(accountDetails);
            accountRepository.save(account);
        }

        @Nested
        @DisplayName("정상적인 요청이면")
        @Order(2)
        class Context_WhenRequestIsValid {

            @Test
            @DisplayName("로그인에 성공한다")
            void it_success_login() throws Exception {
                ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/account/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("type", "local", "email", email, "password", password))));

                result.andExpect(status().isOk());
                result.andExpect(cookie().exists("refreshToken"));
                result.andExpect(jsonPath("$.message").exists());

            }
        }

        @Nested
        @DisplayName("비밀번호가 틀리면")
        @Order(1)
        class Context_WhenPasswordIsInvalid {

            String invalidPassword = ag.getTestPassword();

            @Test
            @DisplayName("로그인에 실패한다")
            void it_returns_password_invalid_error() throws Exception {
                ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/account/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("type", "local", "email", email, "password", invalidPassword))));

                result.andExpect(status().isUnauthorized());
            }
        }

        @Nested
        @DisplayName("존재하지 않는 이메일이면")
        @Order(1)
        class Context_WhenEmailIsNotExists {
            String invalidEmail = ag.getTestEmail();

            @Test
            @DisplayName("로그인에 실패한다")
            void it_returns_email_not_exists_error() throws Exception {
                ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/account/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("type", "local", "email", invalidEmail, "password", password))));

                result.andExpect(status().isUnauthorized());
            }
        }
    }
}
