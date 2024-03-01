package xyz.iknow.authenticaionserver.domain.account.service.oauth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import xyz.iknow.authenticaionserver.domain.account.entity.AccountDetails;
import xyz.iknow.authenticaionserver.domain.account.entity.oauthAccount.OauthAccount;
import xyz.iknow.authenticaionserver.domain.account.entity.oauthAccount.OauthPlatformType;
import xyz.iknow.authenticaionserver.domain.account.repository.AccountRepository;
import xyz.iknow.authenticaionserver.domain.account.repository.oauth.OauthPlatformRepository;
import xyz.iknow.authenticaionserver.security.jwt.service.JwtService;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OauthAccountServiceImpl implements OauthAccountService {
    private final OauthAccountProperties oauthAccountProperties;
    private final AccountRepository accountRepository;
    private final OauthPlatformRepository oauthPlatformRepository;
    private final JwtService jwtService;
    private final RestTemplate restTemplate;

    @Override
    public ResponseEntity<Map> getOauthUrl(String platform) {
        if (Arrays.stream(OauthPlatformType.values()).noneMatch(oauthPlatformType -> oauthPlatformType.name().equals(platform.toUpperCase()))) {
            return ResponseEntity.badRequest().body(Map.of("status", "fail", "message", platform + " is not supported"));
        }

        final String url = oauthAccountProperties.getUrl().get(platform);
        final String clientId = oauthAccountProperties.getClientId().get(platform);
        final String redirectUri = oauthAccountProperties.getRedirectUri().get(platform);

        String loginUrl = url + "?client_id=" + clientId + "&redirect_uri=" + redirectUri + "&response_type=code";
        return ResponseEntity.ok(Map.of("url", loginUrl, "platform", platform, "status", "success"));
    }

    @Override
    public ResponseEntity<Map> login(String platform, String code) {

        if (Arrays.stream(OauthPlatformType.values()).noneMatch(oauthPlatformType -> oauthPlatformType.name().equals(platform.toUpperCase()))) {
            return ResponseEntity.badRequest().body(Map.of("status", "fail", "message", platform + " is not supported"));
        }

        String oauthAccessToken = getAccessToken(platform, code);
        String oauthId = requestOauthId(platform, oauthAccessToken);

        Optional<OauthAccount> maybeAccount = accountRepository.findByOauthId(oauthId);
        OauthAccount account;
        if (maybeAccount.isEmpty()) {
            account = join(platform, oauthId);
        } else {
            account = maybeAccount.get();
        }
        String accessToken = issueToken(account);
        return ResponseEntity.ok(Map.of("accessToken", "Bearer " + accessToken, "status", "success"));
    }

    public String getAccessToken(String platform, String code) {

        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-type", "application/x-www-form-urlencoded");
        headers.add("Accept", "application/json");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", oauthAccountProperties.getClientId().get(platform));
        body.add("redirect_uri", oauthAccountProperties.getRedirectUri().get(platform));
        body.add("code", code);
        final String tokenRequestUrl = oauthAccountProperties.getTokenUrl().get(platform);
        HttpEntity<MultiValueMap> tokenRequest = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.exchange(tokenRequestUrl, HttpMethod.POST, tokenRequest, Map.class);
        return response.getBody().get("access_token").toString();

    }

    public String requestOauthId(String platform, String accessToken) {
        HttpHeaders httpHeaders = new HttpHeaders();

        HttpEntity<Map<String, String>> request = new HttpEntity<>(httpHeaders);
        httpHeaders.add("Authorization", "Bearer " + accessToken);

        ResponseEntity<Map> response = restTemplate.exchange(oauthAccountProperties.getUserInfoUrl().get(platform), HttpMethod.GET, request, Map.class);
        System.out.println(response.getBody());
        return response.getBody().get("id").toString();
    }

    public OauthAccount join(String platform, String platformId) {
        OauthAccount account = OauthAccount.builder()
                .oauthId(platformId)
                .platform(oauthPlatformRepository.findByPlatformType(OauthPlatformType.KAKAO))
                .nickname(platform + "유저#" + RandomStringUtils.random(4, true, true))
                .build();
        AccountDetails accountDetails = new AccountDetails();
        account.setAccountDetails(accountDetails);
        return accountRepository.save(account);
    }

    public String issueToken(OauthAccount account) {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletResponse response = sra.getResponse();

        String accessToken = jwtService.generateAccessToken(account);
        String refreshToken = jwtService.generateRefreshToken(account);

        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return accessToken;
    }
}
