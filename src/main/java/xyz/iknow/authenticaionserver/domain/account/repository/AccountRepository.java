package xyz.iknow.authenticaionserver.domain.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.iknow.authenticaionserver.domain.account.entity.Account;
import xyz.iknow.authenticaionserver.domain.account.entity.oauthAccount.OauthAccount;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);

    Boolean existsByEmail(String email);
    Optional<Account> findByEmailAndPassword(String email, String password);
    Optional<OauthAccount> findByOauthId(String Id);
}
