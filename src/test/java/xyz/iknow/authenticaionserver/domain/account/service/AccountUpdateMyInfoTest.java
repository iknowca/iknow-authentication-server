package xyz.iknow.authenticaionserver.domain.account.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import xyz.iknow.authenticaionserver.domain.account.dto.UpdateAccountForm;
import xyz.iknow.authenticaionserver.domain.account.entity.Account;
import xyz.iknow.authenticaionserver.domain.account.entity.LocalAccount;
import xyz.iknow.authenticaionserver.domain.account.entity.oauthAccount.OauthAccount;
import xyz.iknow.authenticaionserver.domain.account.entity.oauthAccount.OauthPlatformType;
import xyz.iknow.authenticaionserver.security.customUserDetails.CustomUserDetails;
import xyz.iknow.authenticaionserver.test.UnitTest;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DisplayName("AcccountService.updateMyInfo Test")
public class AccountUpdateMyInfoTest extends UnitTest {
    Account account;
    UpdateAccountForm request;
    @BeforeEach
    void setUp() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        request = new UpdateAccountForm();
    }
    @Nested
    @DisplayName("클라이언트가 내 정보를 수정하려고 할 때")
    class Describe_updateMyInfo {
        @Nested
        @DisplayName("Local 계정인 경우")
        class Context_whenLocalAccount {
            @BeforeEach
            void setUp() {
                LocalAccount localAccount = LocalAccount.builder()
                        .email("email")
                        .password("password")
                        .nickname("nickname")
                        .build();
                account = accountRepository.save(localAccount);
                when(authentication.getPrincipal()).thenReturn(CustomUserDetails.builder().account(account).build());

            }
            @Nested
            @DisplayName("비밀번호를 수정하려고 할 때")
            class Context_whenUpdatePassword {
                @BeforeEach
                void setUp() {
                    request.setPassword("newPassword");
                }
                @Nested
                @DisplayName("닉네임을 수정하려고 할 때")
                class Context_whenUpdateNickname {
                    @BeforeEach
                    void setUp() {
                        request.setNickname("newNickname");
                    }
                    @Test
                    @DisplayName("성공 메시지를 반환한다")
                    void it_returnsSuccessMessage() {
                        ResponseEntity<Map> response = accountService.updateMyInfo(request);
                        assertThat(response.getStatusCode().is2xxSuccessful());
                    }
                }
                @Test
                @DisplayName("성공 메시지를 반환한다")
                void it_returnsSuccessMessage() {
                    ResponseEntity<Map> response = accountService.updateMyInfo(request);
                    assertThat(response.getStatusCode().is2xxSuccessful());
                }
            }
            @Nested
            @DisplayName("닉네임을 수정하려고 할 때")
            class Context_whenUpdateNickname {
                @BeforeEach
                void setUp() {
                    request.setNickname("newNickname");
                }
            }
        }
        @Nested
        @DisplayName("Oauth 계정인 경우")
        class Context_whenOauthAccount {
            @BeforeEach
            void setUp() {
                account = accountRepository.save(OauthAccount.builder()
                        .oauthId("oauthId")
                        .platform(oauthPlatformRepository.findByPlatformType(OauthPlatformType.KAKAO))
                        .nickname("nickname")
                        .build());
                when(authentication.getPrincipal()).thenReturn(CustomUserDetails.builder().account(account).build());

            }
            @Nested
            @DisplayName("닉네임을 수정하려고 할 때")
            class Context_whenUpdateNickname {
                @BeforeEach
                void setUp() {
                    request.setNickname("newNickname");
                }
                @Test
                @DisplayName("성공 메시지를 반환한다")
                void it_returnsSuccessMessage() {
                    ResponseEntity<Map> response = accountService.updateMyInfo(request);
                    assertThat(response.getStatusCode().is2xxSuccessful());
                }
            }
        }
    }
}

