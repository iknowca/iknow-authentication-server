package xyz.iknow.authenticaionserver.domain.account.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import xyz.iknow.authenticaionserver.domain.account.dto.AccountDTO;
import xyz.iknow.authenticaionserver.domain.account.dto.LocalAccountDTO;
import xyz.iknow.authenticaionserver.domain.account.dto.oauth.OauthAccountDTO;
import xyz.iknow.authenticaionserver.domain.account.entity.Account;
import xyz.iknow.authenticaionserver.domain.account.entity.LocalAccount;
import xyz.iknow.authenticaionserver.domain.account.entity.oauthAccount.OauthAccount;
import xyz.iknow.authenticaionserver.domain.account.entity.oauthAccount.OauthPlatformType;
import xyz.iknow.authenticaionserver.test.UnitTest;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AcccountService.updateMyInfo Test")
public class AccountUpdateMyInfoTest extends UnitTest {
    Account account;
    AccountDTO request;

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

                request = new LocalAccountDTO();
                request.setType("local");
            }

            @Nested
            @DisplayName("비밀번호를 수정하려고 할 때")
            class Context_whenUpdatePassword {
                @BeforeEach
                void setUp() {
                    ((LocalAccountDTO) request).setPassword("newPassword");
                }

                @Nested
                @DisplayName("닉네임을 수정하려고 할 때")
                class Context_whenUpdateNickname {
                    @BeforeEach
                    void setUp() {
                        request.setNickname("newNickname");
                    }

                    @Test
                    @DisplayName("accountDTO를 반환한다")
                    void it_returnsSuccessMessage() {
                        LocalAccountDTO accountDTO = (LocalAccountDTO) accountService.updateMyInfo(account, request);

                        assertThat(accountDTO.getNickname()).isEqualTo("newNickname");
                        assertThat(accountDTO.getPassword()).isNull();
                        assertThat(accountDTO.getEmail()).isEqualTo("email");
                    }
                }

                @Test
                @DisplayName("accountDTO를 반환한다")
                void it_returnsSuccessMessage() {
                    LocalAccountDTO accountDTO = (LocalAccountDTO) accountService.updateMyInfo(account, request);

                    assertThat(accountDTO.getNickname()).isEqualTo("nickname");
                    assertThat(accountDTO.getPassword()).isNull();
                    assertThat(accountDTO.getEmail()).isEqualTo("email");
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

                request = new OauthAccountDTO();
                request.setType("oauth");
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
                    OauthAccountDTO accountDTO = (OauthAccountDTO) accountService.updateMyInfo(account, request);

                    assertThat(accountDTO.getNickname()).isEqualTo("newNickname");
                }
            }
        }
    }
}

