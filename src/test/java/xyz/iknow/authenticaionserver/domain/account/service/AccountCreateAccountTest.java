package xyz.iknow.authenticaionserver.domain.account.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import xyz.iknow.authenticaionserver.domain.account.dto.LocalAccountDTO;
import xyz.iknow.authenticaionserver.domain.account.exception.AccountException;
import xyz.iknow.authenticaionserver.test.UnitTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("AcccountService.createAccount Test")
public class AccountCreateAccountTest extends UnitTest {
    @Nested
    @DisplayName("AccountDTO가 주어졌을 때")
    class Describe_givenAccountDTO {
        LocalAccountDTO accountDTO = new LocalAccountDTO();
        String email;
        String password;

        @BeforeEach
        void setup() {
            accountDTO.setPassword("password");
        }

        @Nested
        @DisplayName("이메일 형식이 올바른 경우")
        class Context_emailIsInvalid {
            @BeforeEach
            void setup() {
                String email = ag.getTestEmail();
                accountDTO.setEmail(email);
            }

            @Nested
            @DisplayName("이메일이 중복된 경우")
            class Context_emailIsDuplicated {
                @BeforeEach
                void setup() {
                    accountService.createAccount(accountDTO);
                }

                @Test
                @DisplayName("이메일 중복 예외를 발생시킨다.")
                void it_returnsDuplicatedMessage() throws Exception {
                    assertThrows(AccountException.class, () -> {
                        accountService.createAccount(accountDTO);
                    });
                }
            }

            @Nested
            @DisplayName("이메일이 중복되지 않은 경우")
            class Context_emailIsNotDuplicated {
                @Test
                @DisplayName("DB에 저장한다.")
                void it_returnsSuccessMessage() throws Exception {
                    accountService.createAccount(accountDTO);
                    assertThat(accountRepository.existsLocalAccountByEmail(accountDTO.getEmail())).isTrue();
                }
            }

            @Nested
            @DisplayName("이메일 형식이 올바르지 않은 경우")
            class Context_emailIsValid {
                @BeforeEach
                void setup() {
                    accountDTO.setEmail("invalidEmail");
                }

                @Test
                @DisplayName("이메일 형식 예외를 발생시킨다.")
                void it_returnsInvalidEmailMessage() throws Exception {
                    assertThrows(AccountException.class, () -> {
                        accountService.createAccount(accountDTO);
                    });
                }
            }
        }
    }
}
