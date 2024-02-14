package xyz.iknow.authenticaionserver.domain.account.service.oauth;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@ConfigurationProperties(prefix = "oauth")
@Component
@Getter
@Setter
public class OauthAccountProperties {
    private Map<String, String> url;
    private Map<String, String> clientId;
    private Map<String, String> redirectUri;
    private Map<String, String> tokenUrl;
    private Map<String, String> scope;
    private Map<String, String> userInfoUrl;
}
