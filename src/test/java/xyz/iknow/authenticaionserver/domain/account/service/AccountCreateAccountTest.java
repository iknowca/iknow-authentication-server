package xyz.iknow.authenticaionserver.domain.account.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.http.ResponseEntity;
import xyz.iknow.authenticaionserver.domain.account.dto.AccountDTO;
import xyz.iknow.authenticaionserver.test.UnitTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static xyz.iknow.authenticaionserver.test.AccountGenerator.getTestEmail;

@DisplayName("AcccountService.createAccount Test")
public class AccountCreateAccountTest extends UnitTest {
    @Nested
    @DisplayName("AccountDTO가 주어졌을 때")
    class Describe_givenAccountDTO {
        AccountDTO accountDTO = new AccountDTO();
        String email;

        @BeforeEach
        void setup() {
            accountDTO.setPassword("password");
        }

        @Nested
        @DisplayName("이메일 형식이 올바른 경우")
        class Context_emailIsInvalid {
            @BeforeEach
            void setup() {
                String email = getTestEmail();
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
                @DisplayName("이메일 중복 메시지를 반환한다")
                void it_returnsDuplicatedMessage() throws Exception {
                    ResponseEntity response = accountService.createAccount(accountDTO);
                    Assertions.assertThat(response.getBody().equals("이미 가입된 이메일입니다."));
                    Assertions.assertThat(response.getStatusCode().is4xxClientError());
                }
            }

            @Nested
            @DisplayName("이메일이 중복되지 않은 경우")
            class Context_emailIsNotDuplicated {
                @Test
                @DisplayName("성공 메시지를 반환한다")
                void it_returnsSuccessMessage() throws Exception {
                    ResponseEntity response = accountService.createAccount(accountDTO);
                    Assertions.assertThat(response.getBody().equals("성공"));
                    Assertions.assertThat(response.getStatusCode().is2xxSuccessful());
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
                @DisplayName("이메일 형식이 올바르지 않다는 메시지를 반환한다")
                void it_returnsInvalidEmailMessage() throws Exception {
                    ResponseEntity response = accountService.createAccount(accountDTO);
                    Assertions.assertThat(response.getBody().equals("이메일 형식이 올바르지 않습니다."));
                    Assertions.assertThat(response.getStatusCode().is4xxClientError());
                }
            }
        }
    }
}
