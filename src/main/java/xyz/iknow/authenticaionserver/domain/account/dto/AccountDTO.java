package xyz.iknow.authenticaionserver.domain.account.dto;

import lombok.*;
import xyz.iknow.authenticaionserver.domain.account.dto.oauth.OauthPlatformDTO;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
public class AccountDTO {
    private Long id;
    private String email;
    private String password;
    private String nickname;
    private String accountType;
    private OauthPlatformDTO oauthPlatform;
}
