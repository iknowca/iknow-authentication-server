package xyz.iknow.authenticaionserver.domain.account.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import xyz.iknow.authenticaionserver.domain.account.dto.LocalAccountDTO;
import xyz.iknow.authenticaionserver.domain.account.entity.LocalAccount;
import xyz.iknow.authenticaionserver.test.IntegrationTest;

import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("회원가입 테스트")
public class JoinTest extends IntegrationTest {
    String email;
    String password;
    String duplicatedEmail;

    @BeforeEach
    void setUp() {
        email = ag.getTestEmail();
        password = ag.getTestPassword();
        duplicatedEmail = ag.getTestEmail();

        LocalAccount account = LocalAccount.builder()
                .email(duplicatedEmail)
                .password(password)
                .build();
        accountRepository.save(account);
    }

    @Nested
    @DisplayName("클라이언트가 회원가입을 요청할때")
    class Describe_account_join {
        LocalAccountDTO request;

        @BeforeEach
        void setUp() {
            request = LocalAccountDTO.builder()
                    .build();
        }

        @Nested
        @DisplayName("정상적인 요청이라면")
        class Context_WhenRequestIsValid {
            @BeforeEach
            void setUp() {
                request.setEmail(email);
                request.setPassword(password);
            }

            @Test
            @DisplayName("회원가입에 성공한다")
            void it_success_join() throws Exception {
                ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/account/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)));

                result.andExpect(status().isCreated())
                        .andExpect(jsonPath("$.status").value("success"));
            }
        }

        @Nested
        @DisplayName("이메일 중복이라면")
        class Context_WhenEmailIsDuplicated {
            @BeforeEach
            void setUp() {
                request.setEmail(duplicatedEmail);
                request.setPassword(password);
            }

            @DisplayName("이메일 중복 에러를 반환한다")
            @Test
            void it_returns_email_duplicated_error() throws Exception {
                ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/account/join", request)
                        .contentType(MediaType.APPLICATION_JSON));

                result.andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("요청이 비정상적이면 - 이메일 형식이 아님")
        class Context_WhenRequestIsInvalid {
            String invalidEmail = "invalidEmail";

            @BeforeEach
            void setUp() {
                request.setEmail(invalidEmail);
                request.setPassword(password);
            }

            @Test
            @DisplayName("이메일 형식 에러를 반환한다")
            void it_returns_email_format_error() throws Exception {
                ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/account/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("email", invalidEmail, "password", password))));

                result.andExpect(status().isBadRequest());
            }
        }
    }
}
