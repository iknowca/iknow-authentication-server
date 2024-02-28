package xyz.iknow.authenticaionserver.domain.account.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.internal.util.Platform;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import xyz.iknow.authenticaionserver.domain.account.entity.Account;
import xyz.iknow.authenticaionserver.domain.account.entity.LocalAccount;
import xyz.iknow.authenticaionserver.domain.account.repository.oauth.OauthPlatformRepository;
import xyz.iknow.authenticaionserver.security.customUserDetails.CustomUserDetails;
import xyz.iknow.authenticaionserver.test.UnitTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("AcccountService.getMyInfo Test")
public class AccountGetMyInfoTest extends UnitTest {
    @Nested
    @DisplayName("클리아언트가 자신의 정보를 요청할 때")
    class Describe_whenClientRequestMyInfo {
        Account account;
        @Nested
        @DisplayName("계정 정보가 Local 계정인 경우")
        class Context_whenLocalAccount {

            @BeforeEach
            void setup() {
                account = LocalAccount.builder().email("email").password("password").nickname("nickname").build();
                when(securityContext.getAuthentication()).thenReturn(authentication);
                SecurityContextHolder.setContext(securityContext);
                when(authentication.getPrincipal()).thenReturn(CustomUserDetails.builder().account(account).build());

            }
            @Test
            @DisplayName("Email을 포함함 정보를 반환한다.")
            void It_returnsInfoWithEamil() {
                ResponseEntity response = accountService.getMyInfo();
                assertThat(response.getBody().equals(account));
            }
        }
    }
}
