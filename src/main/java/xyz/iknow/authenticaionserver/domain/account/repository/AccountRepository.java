package xyz.iknow.authenticaionserver.domain.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.iknow.authenticaionserver.domain.account.entity.Account;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmail(String email);

    Boolean existsByEmail(String email);
}
