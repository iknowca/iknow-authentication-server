package xyz.iknow.authenticaionserver.domain.account.entity.oauthAccount;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class OauthPlatform {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private OauthPlatformType platformType;

    public OauthPlatform(OauthPlatformType platform) {
        this.platformType = platform;
    }
}
