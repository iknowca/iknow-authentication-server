package xyz.iknow.authenticaionserver.domain.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;
import xyz.iknow.authenticaionserver.domain.account.entity.Account;
import xyz.iknow.authenticaionserver.domain.account.entity.LocalAccount;
import xyz.iknow.authenticaionserver.domain.account.entity.oauthAccount.OauthAccount;
import xyz.iknow.authenticaionserver.domain.account.entity.oauthAccount.OauthPlatform;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("SELECT a FROM LocalAccount a WHERE a.email = :email")
    Optional<LocalAccount> findByEmail(String email);

    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END FROM LocalAccount a WHERE a.email = :email")
    Boolean existsLocalAccountByEmail(String email);

    Optional<OauthAccount> findByOauthId(String Id);

    @Query("SELECT oa.platform FROM OauthAccount oa WHERE oa.id = :accountId")
    OauthPlatform findOauthPlatformByPlatformTypeAndOauthId(Long accountId);

    Optional<LocalAccount> findLocalAccountByEmail(String email);

    @Transactional
    @Modifying
    @Query("update Account a set a.nickname = ?1 where a.id = ?2")
    void updateNicknameById(@NonNull String nickname, Long id);
}
