package xyz.iknow.authenticaionserver.domain.account.dto.oauth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import xyz.iknow.authenticaionserver.domain.account.entity.oauthAccount.OauthPlatform;
import xyz.iknow.authenticaionserver.domain.account.entity.oauthAccount.OauthPlatformType;


@NoArgsConstructor
@Getter
@Setter
public class OauthPlatformDTO {
    private Long id;
    private OauthPlatformType platformType;

    public OauthPlatformDTO(OauthPlatform platform) {
        this.id = platform.getId();
        this.platformType = platform.getPlatformType();
    }
}
