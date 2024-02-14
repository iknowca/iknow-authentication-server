package xyz.iknow.authenticaionserver.domain.account.repository.oauth;


import org.springframework.data.jpa.repository.JpaRepository;
import xyz.iknow.authenticaionserver.domain.account.entity.oauthAccount.OauthPlatform;
import xyz.iknow.authenticaionserver.domain.account.entity.oauthAccount.OauthPlatformType;

public interface OauthPlatformRepository extends JpaRepository<OauthPlatform, Long> {
    OauthPlatform findByPlatformType(OauthPlatformType oauthPlatformType);
}
