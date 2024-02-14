package xyz.iknow.authenticaionserver.security.customUserDetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import xyz.iknow.authenticaionserver.domain.account.entity.Account;
import xyz.iknow.authenticaionserver.domain.account.entity.LocalAccount;
import xyz.iknow.authenticaionserver.domain.account.repository.AccountRepository;

import java.util.Collections;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    final AccountRepository accountRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<LocalAccount> maybeAccount = accountRepository.findByEmail(email);
        if(maybeAccount.isEmpty()) {
            throw new UsernameNotFoundException("There are no account matching the email: "+email);
        }
        LocalAccount account = maybeAccount.get();

        return CustomUserDetails.builder()
                .username(account.getEmail())
                .password(account.getPassword())
                .account(account)
                .authorities(Collections.singleton(new SimpleGrantedAuthority("ROLE_"+"USER")))
                .isEnabled(true)
                .isCredentialsNonExpired(true)
                .isAccountNonLocked(true)
                .isAccountNonExpired(true)
                .build();
    }

    public UserDetails loadUserByAccountId(Long accountId) {
        Optional<Account> maybeAccount = accountRepository.findById(accountId);
        if(maybeAccount.isEmpty()) {
            throw new UsernameNotFoundException("There are no account matching the accountId: "+accountId);
        }
        Account account = maybeAccount.get();

        return CustomUserDetails.builder()
                .account(account)
                .authorities(Collections.singleton(new SimpleGrantedAuthority("ROLE_"+"USER")))
                .isEnabled(true)
                .isCredentialsNonExpired(true)
                .isAccountNonLocked(true)
                .isAccountNonExpired(true)
                .build();
    }
}
