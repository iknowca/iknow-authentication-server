package xyz.iknow.authenticaionserver.domain.account.service.oauth;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface OauthAccountService {
    ResponseEntity<Map> getOauthUrl(String platform);

    ResponseEntity<Map> login(String platform, String code);
}
