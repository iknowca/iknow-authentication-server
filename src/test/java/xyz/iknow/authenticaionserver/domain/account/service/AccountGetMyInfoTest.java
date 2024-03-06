package xyz.iknow.authenticaionserver.domain.account.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import xyz.iknow.authenticaionserver.domain.account.dto.LocalAccountDTO;
import xyz.iknow.authenticaionserver.domain.account.entity.Account;
import xyz.iknow.authenticaionserver.domain.account.entity.LocalAccount;
import xyz.iknow.authenticaionserver.test.UnitTest;

import static org.assertj.core.api.Assertions.assertThat;

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
                accountRepository.save(account);
            }

            @Test
            @DisplayName("Email을 포함함 정보를 반환한다.")
            void It_returnsInfoWithEamil() {
                LocalAccountDTO accountDTO = (LocalAccountDTO) accountService.getMyInfo(account.getId());
                assertThat(accountDTO.getEmail()).isEqualTo("email");
                assertThat(accountDTO.getNickname()).isEqualTo("nickname");
                assertThat(accountDTO.getPassword()).isNull();
            }
        }
    }
}
