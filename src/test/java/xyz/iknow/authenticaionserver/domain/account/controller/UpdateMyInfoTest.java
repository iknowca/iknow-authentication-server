package xyz.iknow.authenticaionserver.domain.account.controller;

import org.junit.jupiter.api.*;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import xyz.iknow.authenticaionserver.domain.account.entity.dto.AccountDTO;
import xyz.iknow.authenticaionserver.domain.account.entity.LocalAccount;
import xyz.iknow.authenticaionserver.domain.account.entity.oauthAccount.OauthAccount;
import xyz.iknow.authenticaionserver.domain.account.entity.oauthAccount.OauthPlatformType;
import xyz.iknow.authenticaionserver.test.MockMvcTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("내 정보 수정 테스트")
public class UpdateMyInfoTest extends MockMvcTest {
    @Nested
    @DisplayName("클라이언트가 내 정보를 수정하려고 할 때")
    class Describe_updateMyInfo {
        static OauthAccount oauthAccount;

        static LocalAccount localAccount;

        @BeforeEach
        void setUp() {
            oauthAccount = OauthAccount.builder()
                    .oauthId("oauthId" + getUniqueId())
                    .platform(oauthPlatformRepository.findByPlatformType(OauthPlatformType.KAKAO))
                    .nickname("oauthDefaultNickname")
                    .build();
            accountRepository.save(oauthAccount);

            localAccount = LocalAccount.builder()
                    .email(getTestEmail())
                    .password(passwordEncoder.encode(getTestPassword()))
                    .nickname("localDefaultNickname")
                    .build();
            accountRepository.save(localAccount);
        }

        AccountDTO request;

        @Nested
        @DisplayName("수정하려는 정보가 없을 경우")
        class Context_noInfoToUpdate {
            @BeforeEach
            void setUp() {
                request = AccountDTO.builder()
                        .build();
            }

            @Test
            @DisplayName("400 Bad Request를 응답한다.")
            void it_responds_400BadRequest() throws Exception {
                ResultActions result = mockMvc.perform(MockMvcRequestBuilders.patch("/account")
                        .header("Authorization", "Bearer " + jwtService.generateAccessToken(localAccount))
                        .content(objectMapper.writeValueAsString(request))
                        .contentType("application/json"));
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
                    request = AccountDTO.builder()
                            .nickname("newNickname")
                            .build();
                }

                @Test
                @DisplayName("200 OK를 응답한다.")
                void it_responds_200OK() throws Exception {
                    ResultActions result = mockMvc.perform(patch("/account")
                            .header("Authorization", "Bearer " + jwtService.generateAccessToken(localAccount))
                            .content(objectMapper.writeValueAsString(request))
                            .contentType("application/json"));

                    result.andExpect(status().isOk());
                }
            }

            @Nested
            @DisplayName("수정하려는 정보가 password만 있을 경우")
            class Context_passwordOnly {
                @BeforeEach
                void setUp() {
                    request = AccountDTO.builder()
                            .password("newPassword")
                            .build();
                }

                @Nested
                @DisplayName("LocalAccount일 경우")
                class Context_localAccount {
                    @Test
                    @DisplayName("200 OK를 응답한다.")
                    void it_responds_200OK() throws Exception {
                        ResultActions result = mockMvc.perform(patch("/account")
                                .header("Authorization", "Bearer " + jwtService.generateAccessToken(localAccount))
                                .content(objectMapper.writeValueAsString(request))
                                .contentType("application/json"));

                        result.andExpect(status().isOk());
                    }
                }

                @Nested
                @DisplayName("OauthAccount일 경우")
                class Context_oauthAccount {
                    @BeforeEach
                    void setUp() {
                        request = AccountDTO.builder()
                                .password("newPassword")
                                .build();
                    }

                    @Test
                    @DisplayName("400 Bad Request를 응답한다.")
                    void it_responds_400BadRequest() throws Exception {
                        ResultActions result = mockMvc.perform(patch("/account")
                                .header("Authorization", "Bearer " + jwtService.generateAccessToken(oauthAccount))
                                .content(objectMapper.writeValueAsString(request))
                                .contentType("application/json"));

                        result.andExpect(status().isBadRequest());
                    }
                }
            }
        }

        @Nested
        @DisplayName("수정하려는 정보가 nickname과 password가 있을 경우")
        class Context_nicknameAndPassword {
            @BeforeEach
            void setUp() {
                request = AccountDTO.builder()
                        .password("newPassword")
                        .nickname("newNickname")
                        .build();
            }

            @Nested
            @DisplayName("LocalAccount일 경우")
            class Context_localAccount {
                @Test
                @DisplayName("200 OK를 응답한다.")
                void it_responds_200OK() throws Exception {
                    ResultActions result = mockMvc.perform(patch("/account")
                            .header("Authorization", "Bearer " + jwtService.generateAccessToken(localAccount))
                            .content(objectMapper.writeValueAsString(request))
                            .contentType("application/json"));

                    result.andExpect(status().isOk());
                }
            }

            @Nested
            @DisplayName("OauthAccount일 경우")
            class Context_oauthAccount {
                @Test
                @DisplayName("400 Bad Request를 응답한다.")
                void it_responds_400BadRequest() throws Exception {
                    ResultActions result = mockMvc.perform(patch("/account")
                            .header("Authorization", "Bearer " + jwtService.generateAccessToken(oauthAccount))
                            .content(objectMapper.writeValueAsString(request))
                            .contentType("application/json"));

                    result.andExpect(status().isBadRequest());
                }

            }
        }
    }
}

