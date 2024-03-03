package xyz.iknow.authenticaionserver.domain.account.controller.oauth;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import xyz.iknow.authenticaionserver.domain.account.entity.oauthAccount.OauthAccount;
import xyz.iknow.authenticaionserver.domain.account.entity.oauthAccount.OauthPlatformType;
import xyz.iknow.authenticaionserver.domain.account.service.oauth.OauthAccountProperties;
import xyz.iknow.authenticaionserver.test.IntegrationTest;

import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("OauthLogin 테스트")
public class OauthLoginTest extends IntegrationTest {
    @Autowired
    OauthAccountProperties oauthAccountProperties;

    @Nested
    @DisplayName("클라이언트가 Authorization Code를 전달하면")
    class Describe_login {
        String authorizationCode = "valid_code";

        @Nested
        @DisplayName("지원하는 플랫폼인 경우 - kakao")
        class Context_kakao {
            String platform = "kakao";
            String oauthId;

            @Nested
            @DisplayName("이미 가입한 회원이라면")
            class Context_already_joined {
                @BeforeEach
                void setUp() throws JsonProcessingException {

                    oauthId = "idOfAMemberWhoIsAlreadyRegistered" + ag.getUniqueId();
                    OauthAccount account = OauthAccount.builder()
                            .oauthId(oauthId)
                            .platform(oauthPlatformRepository.findByPlatformType(OauthPlatformType.KAKAO))
                            .build();
                    accountRepository.save(account);

                    Map<String, String> kakaoResponse = Map.of("id", oauthId);

                    when(restTemplate.exchange(eq(oauthAccountProperties.getTokenUrl().get(platform)), eq(HttpMethod.POST), any(), eq(Map.class))).thenReturn(ResponseEntity.of(Optional.of(Map.of("access_token", "valid_access_token"))));
                    when(restTemplate.exchange(eq(oauthAccountProperties.getUserInfoUrl().get(platform)), eq(HttpMethod.GET), any(), eq(Map.class))).thenReturn(ResponseEntity.of(Optional.of(kakaoResponse)));
                }

                @Test
                @DisplayName("200 OK와 함께 accessToken, refreshToken을 반환한다")
                void it_returns_200_ok_and_accessToken_and_refreshToken() throws Exception {

                    ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/account/oauth/callback/" + platform + "?code=" + authorizationCode))
                            .andExpect(status().isOk());
                    result.andExpect(jsonPath("$.data").exists());
                    result.andExpect(cookie().exists("refreshToken"));
                    verify(restTemplate, times(1)).exchange(eq(oauthAccountProperties.getTokenUrl().get(platform)), eq(HttpMethod.POST), any(), eq(Map.class));
                    verify(restTemplate, times(1)).exchange(eq(oauthAccountProperties.getUserInfoUrl().get(platform)), eq(HttpMethod.GET), any(), eq(Map.class));
                }
            }

            @Nested
            @DisplayName("회원가입이 필요한 경우")
            class Context_need_join {
                String oauthId = "idOfAMemberWhoIsNotRegistered";

                @BeforeEach
                void setUp() throws JsonProcessingException {

                    oauthId = oauthId + ag.getUniqueId();

                    Map<String, String> kakaoResponse = Map.of("id", oauthId);

                    when(restTemplate.exchange(eq(oauthAccountProperties.getTokenUrl().get(platform)), eq(HttpMethod.POST), any(), eq(Map.class))).thenReturn(ResponseEntity.of(Optional.of(Map.of("access_token", "valid_access_token"))));
                    when(restTemplate.exchange(eq(oauthAccountProperties.getUserInfoUrl().get(platform)), eq(HttpMethod.GET), any(), eq(Map.class))).thenReturn(ResponseEntity.of(Optional.of(kakaoResponse)));
                    verify(accountRepository, times(0)).save(any());
                }

                @Test
                @DisplayName("200 OK와 함께 accessToken, refreshToken을 반환한다")
                void it_returns_200_ok() throws Exception {

                    ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/account/oauth/callback/" + platform + "?code=" + authorizationCode))
                            .andExpect(status().isOk());
                    result.andExpect(jsonPath("$.data").exists());
                    result.andExpect(cookie().exists("refreshToken"));

                    verify(restTemplate, times(1)).exchange(eq(oauthAccountProperties.getTokenUrl().get(platform)), eq(HttpMethod.POST), any(), eq(Map.class));
                    verify(restTemplate, times(1)).exchange(eq(oauthAccountProperties.getUserInfoUrl().get(platform)), eq(HttpMethod.GET), any(), eq(Map.class));
                    verify(accountRepository, times(1)).save(any());
                }
            }
        }

        @Nested
        @DisplayName("플랫폼이 null인 경우")
        class Context_null {
            String platform = null;

            @Test
            @DisplayName("400 Bad Request를 반환한다")
            void it_returns_400_bad_request() throws Exception {
                mockMvc.perform(MockMvcRequestBuilders.get("/account/oauth/callback/" + platform + "?code=1234"))
                        .andExpect(status().isBadRequest());
            }
        }

        @Nested
        @DisplayName("지원하지 않는 플랫폼인 경우 - github")
        class Context_unsupported_platform {
            String platform = "github";

            @Test
            @DisplayName("400 Bad Request를 반환한다")
            void it_returns_400_bad_request() throws Exception {
                mockMvc.perform(MockMvcRequestBuilders.get("/account/oauth/callback/" + platform + "?code=1234"))
                        .andExpect(status().isBadRequest());
            }
        }
    }
}
