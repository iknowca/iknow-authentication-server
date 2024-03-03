package xyz.iknow.authenticaionserver.domain.account.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import xyz.iknow.authenticaionserver.domain.account.entity.LocalAccount;
import xyz.iknow.authenticaionserver.domain.account.exception.AccountException;
import xyz.iknow.authenticaionserver.test.UnitTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("AcccountService.validateEmail Test")
public class AccountValidateEmailTest extends UnitTest {
    @Nested
    @DisplayName("Given email")
    class Describe_givenEmail {

        String email = ag.getTestEmail();

        @Nested
        @DisplayName("When email is valid")
        class Context_emailIsValid {

            @DisplayName("When email is duplicated")
            class Context_emailIsDuplicated {
                @BeforeEach
                void setup() {
                    LocalAccount localAccount = new LocalAccount();
                    localAccount.setEmail(email);
                    accountRepository.save(localAccount);
                }

                @Test
                @DisplayName("Then return false")
                void it_returnsFalse() {
                    assertThat(!accountService.validateEamil(email));
                }
            }

            @Nested
            @DisplayName("When email is not duplicated")
            class Context_emailIsNotDuplicated {
                @Test
                @DisplayName("Then return true")
                void it_returnsTrue() {
                    assertThat(accountService.validateEamil(email));
                }
            }
        }

        @Nested
        @DisplayName("When email is invalid")
        class Context_emailIsInvalid {
            @BeforeEach
            void setup() {
                email = "invalidEmail";
            }

            @Test
            @DisplayName("Then return false")
            void it_returnsFalse() {
                assertThrows(AccountException.class, ()-> accountService.validateEamil(email));
            }
        }
    }
}
