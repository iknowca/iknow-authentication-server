package xyz.iknow.authenticaionserver.domain.account.entity;

import lombok.*;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
public class AccountDTO {
    private Long id;
    private String email;
    private String password;
    private String nickname;
    private String oauthPlatform;
}
