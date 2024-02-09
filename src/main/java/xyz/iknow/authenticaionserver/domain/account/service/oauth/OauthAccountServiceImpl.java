package xyz.iknow.authenticaionserver.domain.account.service.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OauthAccountServiceImpl implements OauthAccountService {
    private final OauthAccountProperties oauthAccountProperties;

    @Override
    public ResponseEntity<Map> getOauthUrl(String platform) {

        final String url = oauthAccountProperties.getUrl().get(platform);
        final String clientId = oauthAccountProperties.getClientId().get(platform);
        final String redirectUri = oauthAccountProperties.getRedirectUri().get(platform);

        String loginUrl = url + "?client_id=" + clientId + "&redirect_uri=" + redirectUri + "&response_type=code";
        return ResponseEntity.ok(Map.of("url", loginUrl, "platform", platform, "status", "success"));
    }

    @Override
    public ResponseEntity<Map> login(String platform, String code) {
        String accessToken = getAccessToken(platform, code);

        return null;
    }

    public String getAccessToken(String platform, String code) {

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-type", "application/x-www-form-urlencoded");
        headers.add("Accept", "application/json");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", oauthAccountProperties.getClientId().get(platform));
        body.add("redirect_uri", oauthAccountProperties.getRedirectUri().get(platform));
        body.add("code", code);
        System.out.println(body);
        final String tokenRequestUrl = oauthAccountProperties.getTokenUrl().get(platform);
        HttpEntity<MultiValueMap> tokenRequest = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.exchange(tokenRequestUrl, HttpMethod.POST, tokenRequest, Map.class);

        return response.getBody().get("access_token").toString();

    }
}
