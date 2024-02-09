package xyz.iknow.authenticaionserver.domain.account.repository.oauth;


import org.springframework.data.jpa.repository.JpaRepository;
import xyz.iknow.authenticaionserver.domain.account.entity.oauthAccount.OauthPlatform;

public interface OauthPlatformRepository extends JpaRepository<OauthPlatform, Long> {
}
