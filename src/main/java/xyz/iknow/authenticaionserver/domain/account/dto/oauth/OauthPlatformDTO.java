package xyz.iknow.authenticaionserver.domain.account.dto.oauth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import xyz.iknow.authenticaionserver.domain.account.entity.oauthAccount.OauthPlatformType;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
public class OauthPlatformDTO implements Serializable {
    private Long id;
    private OauthPlatformType platformType;
}
