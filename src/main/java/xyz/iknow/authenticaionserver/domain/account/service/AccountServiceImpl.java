package xyz.iknow.authenticaionserver.domain.account.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import xyz.iknow.authenticaionserver.domain.account.entity.Account;
import xyz.iknow.authenticaionserver.domain.account.entity.AccountDTO;
import xyz.iknow.authenticaionserver.domain.account.repository.AccountRepository;
import xyz.iknow.authenticaionserver.domain.jwt.service.JwtService;
import xyz.iknow.authenticaionserver.security.customUserDetails.CustomUserDetails;
import xyz.iknow.authenticaionserver.utility.redis.token.TokenService.TokenService;
import xyz.iknow.authenticaionserver.utility.redis.token.token.RefreshToken;
import xyz.iknow.authenticaionserver.utility.validator.EmailValidator;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    final private AccountRepository accountRepository;
    final private EmailValidator emailValidator;
    final private JwtService jwtService;
    final private TokenService tokenService;
    final private BCryptPasswordEncoder passwordEncoder;

    @Override
    public Boolean validateEamil(String email) {
         Boolean result = accountRepository.existsByEmail(email);
        return result;
    }

    @Override
    public ResponseEntity<Map> createAccount(AccountDTO request) {
        String email = request.getEmail();
        String password = request.getPassword();

        if (!emailValidator.validate(email)) {
            return ResponseEntity.badRequest().body(Map.of("message", "이메일 형식이 올바르지 않습니다."));
        }


        if (accountRepository.existsByEmail(email)) {
            return ResponseEntity.badRequest().body(Map.of("message", "이미 가입된 이메일입니다."));
        }

        Account account = Account.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();
        accountRepository.save(account);

        return ResponseEntity.ok().body(Map.of("message", "회원가입에 성공했습니다.", "status", "success"));
    }

    @Override
    public ResponseEntity<Map> refresh(Map request) {
        String refreshToken = (String) request.get("refreshToken");
        Map<String, Object> values = jwtService.parseToken(refreshToken);
        if (values == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "토큰이 유효하지 않습니다.", "status", "fail"));
        }
        Long accountId = (Long) values.get("accountId");

        Optional<RefreshToken> validToken = tokenService.findRefreshTokenById(accountId);
        if (validToken.isEmpty() || !validToken.get().getJwt().equals(refreshToken.substring(7))) {
            return ResponseEntity.badRequest().body(Map.of("message", "토큰이 유효하지 않습니다.", "status", "fail"));
        }

        Optional<Account> maybeAccount = accountRepository.findById(accountId);
        if (maybeAccount.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "토큰이 유효하지 않습니다.", "status", "fail"));
        }
        Account account = maybeAccount.get();

        return ResponseEntity.ok(Map.of("status", "success",
                "accessToken", "Bearer " + jwtService.generateAccessToken(account)));
    }

    @Override
    public ResponseEntity<AccountDTO> getMyInfo() {
        Account account = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAccount();
        return ResponseEntity.ok(AccountDTO.builder()
                .id(account.getId())
                .email(account.getEmail())
                .nickname(account.getNickname())
                .build());
    }
}
