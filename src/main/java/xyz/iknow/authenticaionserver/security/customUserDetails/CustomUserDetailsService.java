package xyz.iknow.authenticaionserver.security.customUserDetails;

import io.jsonwebtoken.JwtException;
import jakarta.transaction.Transactional;
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
import xyz.iknow.authenticaionserver.security.jwt.exception.TokenException;
import xyz.iknow.authenticaionserver.utility.redis.token.TokenRepository.AccessTokenRepository;
import xyz.iknow.authenticaionserver.utility.redis.token.token.AccessToken;

import java.util.Collections;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final AccessTokenRepository accessTokenRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        AccessToken accessToken = accessTokenRepository.findById(Long.parseLong(id)).orElseThrow(() -> new TokenException(TokenException.TOKEN_ERROR.EXPIRED_TOKEN));

        Account account = Account.builder().id(Long.parseLong(id)).build();

        return CustomUserDetails.builder()
                .username(id)
                .password(accessToken.getJwt())
                .authorities(Collections.singleton(new SimpleGrantedAuthority("ROLE_" + "USER")))
                .isEnabled(true)
                .isCredentialsNonExpired(true)
                .isAccountNonLocked(true)
                .isAccountNonExpired(!account.getAccountDetails().getWithDraw())
                .build();
    }
}
