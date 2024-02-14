package xyz.iknow.authenticaionserver.domain.account.controller.oauth;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import xyz.iknow.authenticaionserver.test.MockMvcTest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("OauthUrl 요청 테스트")
public class GetOauthUrlTest extends MockMvcTest {
    @Nested
    @DisplayName("클라이언트가 OauthUrl을 요청하면")
    class Describe_getOauthUrl {
        String url = "/account/oauth/url/";
        @Nested
        @DisplayName("지원하는 플랫폼인 경우 - kakao")
        class Context_kakao {
            String platform = "kakao";
            @DisplayName("카카오 로그인 URL을 반환한다")
            @Test
            void it_returns_kakao_login_url() throws Exception {
                mockMvc.perform(MockMvcRequestBuilders.get(url + platform))
                        .andExpect(status().isOk());
            }
        }
        @Nested
        @DisplayName("플랫폼이 null인 경우")
        class Context_null {
            String platform = null;
            @DisplayName("400 Bad Request를 반환한다")
            @Test
            void it_returns_400_bad_request() throws Exception {
                mockMvc.perform(MockMvcRequestBuilders.get(url + platform))
                        .andExpect(status().isBadRequest());
            }
        }
        @Nested
        @DisplayName("지원하지 않는 플랫폼인 경우 - github")
        class Context_unsupported_platform {
            String platform = "github";
            @DisplayName("400 Bad Request를 반환한다")
            @Test
            void it_returns_400_bad_request() throws Exception {
                mockMvc.perform(MockMvcRequestBuilders.get(url + platform))
                        .andExpect(status().isBadRequest());
            }
        }
    }
}
