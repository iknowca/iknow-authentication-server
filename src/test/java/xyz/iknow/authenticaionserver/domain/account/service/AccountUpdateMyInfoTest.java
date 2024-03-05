package xyz.iknow.authenticaionserver.domain.account.service;

import org.junit.jupiter.api.*;
import xyz.iknow.authenticaionserver.domain.account.dto.AccountDTO;
import xyz.iknow.authenticaionserver.domain.account.dto.LocalAccountDTO;
import xyz.iknow.authenticaionserver.domain.account.dto.oauth.OauthAccountDTO;
import xyz.iknow.authenticaionserver.domain.account.entity.Account;
import xyz.iknow.authenticaionserver.domain.account.entity.LocalAccount;
import xyz.iknow.authenticaionserver.domain.account.entity.oauthAccount.OauthAccount;
import xyz.iknow.authenticaionserver.domain.account.entity.oauthAccount.OauthPlatformType;
import xyz.iknow.authenticaionserver.domain.account.exception.AccountException;
import xyz.iknow.authenticaionserver.test.UnitTest;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AcccountService.updateMyInfo Test")
public class AccountUpdateMyInfoTest extends UnitTest {
    Account account;
    AccountDTO request;

    @Nested
    @DisplayName("account와 request가 주어진다면")
    class Describe_account_and_request {
        String nickname;
        String newNickname;
        String password;
        String email;
        Account account;
        AccountDTO request;
        @BeforeEach
        void setUp() {
            nickname = ag.getTestNickname();
            password = ag.getTestPassword();
            email = ag.getTestEmail();
            account = LocalAccount.builder()
                    .nickname(nickname)
                    .password(passwordEncoder.encode(password))
                    .email(email)
                    .build();
            accountRepository.save(account);

            request = new AccountDTO();
        }

        @Nested
        @DisplayName("새로운 닉네임이 request에 주어진다면")
        class Context_with_new_nickname {
            @BeforeEach
            void setUp() {
                newNickname = ag.getTestNickname();
                request.setNickname(newNickname);
            }
            @Test
            @DisplayName("닉네임이 변경된다")
            void it_updates_nickname() {
                accountService.updateMyInfo(account, request);
                assertThat(account.getNickname()).isEqualTo(newNickname);
            }
        }
        @Nested
        @DisplayName("닉네임이 주어지지 않는다면")
        class Context_nickname_not_given {
            @Test
            @DisplayName("예외가 발생한다.")
            void it_does_not_update_nickname() {
                Assertions.assertThrows(AccountException.class, () -> accountService.updateMyInfo(account, request));
            }
        }
    }
}

