package xyz.iknow.authenticaionserver.domain.account.service.oauth;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface OauthAccountService {
    String getOauthUrl(String platform);

    String login(String platform, String code);
}
