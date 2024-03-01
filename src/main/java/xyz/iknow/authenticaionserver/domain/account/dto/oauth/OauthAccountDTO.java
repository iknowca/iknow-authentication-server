package xyz.iknow.authenticaionserver.domain.account.dto.oauth;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import xyz.iknow.authenticaionserver.domain.account.dto.AccountDTO;
import xyz.iknow.authenticaionserver.domain.account.entity.oauthAccount.OauthAccount;

@Setter
@Getter
@SuperBuilder
public class OauthAccountDTO extends AccountDTO {
    private String oauthId;
    private OauthPlatformDTO oauthPlatform;

    public OauthAccountDTO(OauthAccount account) {
        super(account);
        this.oauthId = account.getOauthId();
    }
}
