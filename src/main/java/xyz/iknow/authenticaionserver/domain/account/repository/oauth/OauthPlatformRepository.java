package xyz.iknow.authenticaionserver.domain.account.repository.oauth;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import xyz.iknow.authenticaionserver.domain.account.entity.oauthAccount.OauthAccount;
import xyz.iknow.authenticaionserver.domain.account.entity.oauthAccount.OauthPlatform;
import xyz.iknow.authenticaionserver.domain.account.entity.oauthAccount.OauthPlatformType;

public interface OauthPlatformRepository extends JpaRepository<OauthPlatform, Long> {
    OauthPlatform findByPlatformType(OauthPlatformType oauthPlatformType);

    @Query("select oa.platform.platformType from OauthAccount oa where oa.id = :accountId")
    OauthPlatformType findByAccountId(Long accountId);
}
