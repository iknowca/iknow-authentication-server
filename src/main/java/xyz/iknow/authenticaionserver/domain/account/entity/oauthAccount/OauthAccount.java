package xyz.iknow.authenticaionserver.domain.account.entity.oauthAccount;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
