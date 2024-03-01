package xyz.iknow.authenticaionserver.domain.account.dto.oauth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import xyz.iknow.authenticaionserver.domain.account.dto.AccountDTO;
import xyz.iknow.authenticaionserver.domain.account.entity.oauthAccount.OauthAccount;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
public class OauthAccountDTO extends AccountDTO {
    private String oauthId;
    private OauthPlatformDTO oauthPlatform;

    public OauthAccountDTO(OauthAccount account) {
        super(account);
        this.oauthId = account.getOauthId();
    }
}
