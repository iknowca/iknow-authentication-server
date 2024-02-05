package xyz.iknow.authenticaionserver.security.customUserDetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import xyz.iknow.authenticaionserver.domain.account.entity.Account;
import xyz.iknow.authenticaionserver.domain.account.repository.AccountRepository;
import xyz.iknow.authenticaionserver.security.customFilter.AccountContext;

import java.util.Collections;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    final AccountRepository accountRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("loadUserByUsername: "+email);
        Optional<Account> maybeAccount = accountRepository.findByEmail(email);
        if(maybeAccount.isEmpty()) {
            throw new UsernameNotFoundException("There are no account matching the email: "+email);
        }
        Account account = maybeAccount.get();

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
}
