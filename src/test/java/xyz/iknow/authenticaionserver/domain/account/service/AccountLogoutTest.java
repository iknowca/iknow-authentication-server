package xyz.iknow.authenticaionserver.domain.account.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import xyz.iknow.authenticaionserver.domain.account.dto.AccountDTO;
import xyz.iknow.authenticaionserver.domain.account.entity.Account;
import xyz.iknow.authenticaionserver.security.customUserDetails.CustomUserDetails;
import xyz.iknow.authenticaionserver.test.AccountGenerator;
import xyz.iknow.authenticaionserver.test.UnitTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@DisplayName("AcccountService.logout Test")
public class AccountLogoutTest extends UnitTest {
    Account account;
    @BeforeEach
    void setup() throws Exception {
        String email = AccountGenerator.getTestEmail();
        String password = AccountGenerator.getTestPassword();
        mockMvc.perform(post("/account/join")
                .content(objectMapper.writeValueAsString(AccountDTO.builder()
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
        @DisplayName("로그아웃 성공")
        class Context_logoutSuccess {
            @BeforeEach
            void setup() {
                SecurityContextHolder.setContext(securityContext);
                when(securityContext.getAuthentication()).thenReturn(authentication);
                when(authentication.getPrincipal()).thenReturn(CustomUserDetails.builder().account(account).build());
            }

            @Test
            @DisplayName("로그아웃 성공 메시지를 반환한다")
            void it_returnsSuccessMessage() {
                ResponseEntity response = accountService.logout();
                assertThat(response.getBody().equals("로그아웃 성공"));
                assertThat(response.getStatusCode().is2xxSuccessful());
            }
        }
    }
}
