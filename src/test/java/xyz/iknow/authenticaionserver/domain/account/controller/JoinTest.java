package xyz.iknow.authenticaionserver.domain.account.controller;

import org.junit.jupiter.api.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import xyz.iknow.authenticaionserver.domain.account.entity.LocalAccount;
import xyz.iknow.authenticaionserver.test.IntegrationTest;

import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("회원가입 테스트")
public class JoinTest extends IntegrationTest {
    @Nested
    @DisplayName("클라이언트가 회원가입을 요청할때")
    class Describe_account_join {
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
        @DisplayName("정상적인 요청이라면")
        class Context_WhenRequestIsValid {
            @Test
            @DisplayName("회원가입에 성공한다")
            void it_success_join() throws Exception {
                ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/account/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("email", email, "password", password))));

                result.andExpect(status().isOk())
                        .andExpect(jsonPath("$.status").value("success"));
            }
        }

        @Nested
        @DisplayName("이메일 중복이라면")
        class Context_WhenEmailIsDuplicated {
            @DisplayName("이메일 중복 에러를 반환한다")
            @Test
            void it_returns_email_duplicated_error() throws Exception {
                ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/account/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("email", duplicatedEmail, "password", password))));

                result.andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.message").value("이미 가입된 이메일입니다."));
            }
        }

        @Nested
        @DisplayName("요청이 비정상적이면 - 이메일 형식이 아님")
        class Context_WhenRequestIsInvalid {
            String invalidEmail = "invalidEmail";

            @Test
            @DisplayName("이메일 형식 에러를 반환한다")
            void it_returns_email_format_error() throws Exception {
                ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/account/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("email", invalidEmail, "password", password))));

                result.andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.message").value("이메일 형식이 올바르지 않습니다."));
            }
        }
    }
}
