package xyz.iknow.authenticaionserver.domain.account.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import xyz.iknow.authenticaionserver.domain.account.dto.LocalAccountDTO;
import xyz.iknow.authenticaionserver.domain.account.dto.oauth.OauthAccountDTO;
import xyz.iknow.authenticaionserver.domain.account.entity.Account;
import xyz.iknow.authenticaionserver.domain.account.entity.oauthAccount.OauthAccount;
import xyz.iknow.authenticaionserver.domain.account.entity.oauthAccount.OauthPlatformType;
import xyz.iknow.authenticaionserver.test.IntegrationTest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("내 정보 수정 테스트")
public class UpdateMyInfoTest extends IntegrationTest {
    @Nested
    @DisplayName("클라이언트가 내 정보를 수정하려고 할 때")
    class Describe_updateMyInfo {
        String email;
        String password;
        String updatedPassword;
        String nickname;
        String updatedNickname;
        String accessToken;

        @BeforeEach
        void setUp() {
            email = ag.getTestEmail();
            password = ag.getTestPassword();
            updatedPassword = ag.getTestPassword();
            nickname = ag.getTestNickname();
            updatedNickname = ag.getTestNickname();
        }

        @Nested
        @DisplayName("Oauth 계정인 경우")
        class Context_whenOauthAccount {
            OauthAccountDTO request;

            @BeforeEach
            void setUp() throws Exception {
                OauthAccount oauthAccount = OauthAccount.builder()
                        .oauthId("oauthId" + ag.getUniqueId())
                        .platform(oauthPlatformRepository.findByPlatformType(OauthPlatformType.KAKAO))
                        .nickname(nickname)
                        .build();

                Account account = accountRepository.save(oauthAccount);
                accessToken = jwtService.generateAccessToken(account);

                request = OauthAccountDTO.builder()
                        .type("oauth")
                        .oauthId("fdsafdsa")
                        .build();
            }

            @Nested
            @DisplayName("수정하려는 정보가 없을 경우")
            class Context_noInfoToUpdate {
                @BeforeEach
                void setUp() {
                }

                @Test
                @DisplayName("400 Bad Request를 응답한다.")
                void it_responds_400BadRequest() throws Exception {
                    ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put("/account")
                            .header("Authorization", "Bearer " + accessToken)
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(request)));

                    result.andExpect(status().isBadRequest());
                }
            }

            @Nested
            @DisplayName("수정하려는 정보가 있을 경우")
            class Context_hasInfoToUpdate {
                @Nested
                @DisplayName("수정하려는 정보가 nickname만 있을 경우")
                class Context_nicknameOnly {
                    @BeforeEach
                    void setUp() {
                        request.setNickname(updatedNickname);
                    }

                    @Test
                    @DisplayName("200 OK를 응답한다.")
                    void it_responds_200OK() throws Exception {
                        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put("/account")
                                .header("Authorization", "Bearer " + accessToken)
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(request)));

                        result.andExpect(status().isOk());
                    }
                }
            }
        }

        @Nested
        @DisplayName("Local 계정인 경우")
        class Context_whenLocalAccount {
            LocalAccountDTO request;

            @BeforeEach
            void setUp() throws Exception {
                request = LocalAccountDTO.builder()
                        .email(email)
                        .nickname(nickname)
                        .password(passwordEncoder.encode(password))
                        .type("local")
                        .build();
                accountService.createAccount(request);
                Account account = accountRepository.findByEmail(email).get();
                accessToken = jwtService.generateAccessToken(account);
            }

            @Nested
            @DisplayName("수정하려는 정보가 없을 경우")
            class Context_noInfoToUpdate {
                LocalAccountDTO request;

                @BeforeEach
                void setUp() {
                    request = LocalAccountDTO.builder()
                            .type("local")
                            .build();
                }

                @Test
                @DisplayName("400 Bad Request를 응답한다.")
                void it_responds_400BadRequest() throws Exception {
                    ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put("/account")
                            .header("Authorization", "Bearer " + accessToken)
                            .contentType("application/json")
                            .content(objectMapper.writeValueAsString(request)));

                    result.andExpect(status().isBadRequest());
                }

                @Nested
                @DisplayName("수정하려는 정보가 있을 경우")
                class Context_hasInfoToUpdate {
                    @Nested
                    @DisplayName("수정하려는 정보가 nickname만 있을 경우")
                    class Context_nicknameOnly {

                        @BeforeEach
                        void setUp() {
                            request.setNickname(ag.getTestNickname());
                        }

                        @Test
                        @DisplayName("200 OK를 응답한다.")
                        void it_responds_200OK() throws Exception {
                            ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put("/account")
                                    .header("Authorization", "Bearer " + accessToken)
                                    .contentType("application/json")
                                    .content(objectMapper.writeValueAsString(request)));

                            result.andExpect(status().isOk());
                        }
                    }

                    @Nested
                    @DisplayName("수정하려는 정보가 password만 있을 경우")
                    class Context_passwordOnly {
                        @BeforeEach
                        void setUp() {
                            request.setPassword(ag.getTestPassword());
                        }

                        @Test
                        @DisplayName("200 OK를 응답한다.")
                        void it_responds_200OK() throws Exception {
                            ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put("/account")
                                    .header("Authorization", "Bearer " + accessToken)
                                    .contentType("application/json")
                                    .content(objectMapper.writeValueAsString(request)));

                            result.andExpect(status().isOk());
                        }
                    }

                    @Nested
                    @DisplayName("수정하려는 정보가 nickname과 password가 있을 경우")
                    class Context_nicknameAndPassword {
                        @BeforeEach
                        void setUp() {
                            request.setNickname(ag.getTestNickname());
                            request.setPassword(ag.getTestPassword());
                        }

                        @Test
                        @DisplayName("200 OK를 응답한다.")
                        void it_responds_200OK() throws Exception {
                            ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put("/account")
                                    .header("Authorization", "Bearer " + accessToken)
                                    .contentType("application/json")
                                    .content(objectMapper.writeValueAsString(request)));

                            result.andExpect(status().isOk());
                        }
                    }
                }
            }
        }
    }
}
