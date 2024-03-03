package xyz.iknow.authenticaionserver.domain.account.controller;

import com.jayway.jsonpath.JsonPath;
import io.swagger.v3.core.util.Json;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import xyz.iknow.authenticaionserver.domain.account.entity.LocalAccount;
import xyz.iknow.authenticaionserver.test.IntegrationTest;

import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("이메일 계정 확인 테스트")
public class ValidateEmailTest extends IntegrationTest {

    @Nested
    @DisplayName("클라이언트가 이메일 중복검사를 요청할때")
    class Describe_account_validate_email {
        @BeforeEach
        void setUp() {
            LocalAccount account = LocalAccount.builder()
                    .email(duplicatedEmail)
                    .password("password")
                    .build();
            accountRepository.save(account);
        }

        String duplicatedEmail = ag.getTestEmail();

        @Nested
        @DisplayName("중복되지 않은 이메일을 입력하면")
        class Context_not_duplicated_email {
            String validateEmail = ag.getTestEmail();

            @Test
            @DisplayName("success를 반환한다")
            void it_returns_true() throws Exception {
                System.out.println("validateEmail = " + validateEmail);

                ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/account/validate-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("email", validateEmail, "type", "local"))));

                result.andExpect(status().isOk())
                        .andExpect(jsonPath("$.status").value("success"));

            }
        }

        @Nested
        @DisplayName("중복된 이메일을 입력하면")
        class Context_duplicated_email {

            @Test
            @DisplayName("true를 반환한다")
            void it_returns_false() throws Exception {
                ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/account/validate-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("email", duplicatedEmail, "type", "local"))));

                result.andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.status").value("failure"));

            }
        }
    }
}
