package xyz.iknow.authenticaionserver.domain.account.entity.oauthAccount;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import xyz.iknow.authenticaionserver.domain.account.entity.Account;

@Entity
@Getter
@Setter
@DiscriminatorValue("ouath")
public class OauthAccount extends Account {
    private String oauthId;
    @ManyToOne(fetch = FetchType.LAZY)
    private OauthPlatform platform;
}
