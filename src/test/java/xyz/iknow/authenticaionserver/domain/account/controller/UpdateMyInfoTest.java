package xyz.iknow.authenticaionserver.domain.account.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import xyz.iknow.authenticaionserver.domain.account.dto.AccountDTO;
import xyz.iknow.authenticaionserver.domain.account.dto.LocalAccountDTO;
import xyz.iknow.authenticaionserver.domain.account.dto.oauth.OauthAccountDTO;
import xyz.iknow.authenticaionserver.domain.account.entity.Account;
import xyz.iknow.authenticaionserver.domain.account.entity.LocalAccount;
import xyz.iknow.authenticaionserver.domain.account.entity.oauthAccount.OauthAccount;
import xyz.iknow.authenticaionserver.domain.account.entity.oauthAccount.OauthPlatformType;
import xyz.iknow.authenticaionserver.test.IntegrationTest;

import static org.assertj.core.api.Assertions.assertThat;
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
        AccountDTO request;

        @BeforeEach
        void setUp() {
            email = ag.getTestEmail();
            password = ag.getTestPassword();
            nickname = ag.getTestNickname();
            updatedNickname = ag.getTestNickname();
        }

        @Nested
        @DisplayName("nickname만 수정하려고 하면")
        class Context_with_nickname_only {
            @BeforeEach
            void setUp() throws Exception {

                Account account = accountRepository.save(LocalAccount.builder()
                        .email(email)
                        .password(ag.passwordEncoder().encode(password))
                        .nickname(nickname)
                        .build());
                accessToken = jwtService.generateAccessToken(account);

                request = new LocalAccountDTO();
                request.setType("local");
                request.setNickname(updatedNickname);
            }
            @Test
            @DisplayName("닉네임이 수정된다")
            void It_updates_nickname() throws Exception {

                ResultActions result = mockMvc.perform(MockMvcRequestBuilders.patch("/account")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)));

                result.andExpect(status().isOk());
                accountRepository.findByEmail(email).ifPresent(account -> {
                    assertThat(account.getNickname()).isEqualTo(updatedNickname);
                });
            }
        }
    }
}
