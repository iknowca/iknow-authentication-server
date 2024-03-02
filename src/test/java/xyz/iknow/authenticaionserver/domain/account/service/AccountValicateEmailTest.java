package xyz.iknow.authenticaionserver.domain.account.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import xyz.iknow.authenticaionserver.domain.account.entity.LocalAccount;
import xyz.iknow.authenticaionserver.test.UnitTest;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AcccountService.validateEmail Test")
public class AccountValicateEmailTest extends UnitTest {
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
                @DisplayName("Then return false")
                void it_returnsFalse() {
                    Boolean result = accountService.validateEamil(email);
                    assertThat(result).isFalse();
                }
            }

            @Nested
            @DisplayName("When email is not duplicated")
            class Context_emailIsNotDuplicated {
                @DisplayName("Then return true")
                void it_returnsTrue() {
                    Boolean result = accountService.validateEamil(email);
                    assertThat(result).isTrue();
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
            @DisplayName("Then return false")
            void it_returnsFalse() {
                Boolean result = accountService.validateEamil(email);
                assertThat(result).isFalse();
            }
        }
    }
}
