package xyz.iknow.authenticaionserver.domain.account.entity.oauthAccount;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import xyz.iknow.authenticaionserver.domain.account.entity.Account;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@DiscriminatorValue("ouath")
public class OauthAccount extends Account {
    private String oauthId;
    @ManyToOne(fetch = FetchType.LAZY)
    private OauthPlatform platform;
}
