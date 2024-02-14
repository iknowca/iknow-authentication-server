package xyz.iknow.authenticaionserver.domain.account.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import xyz.iknow.authenticaionserver.domain.account.entity.Account;
import xyz.iknow.authenticaionserver.domain.account.entity.AccountDTO;
import xyz.iknow.authenticaionserver.domain.account.entity.LocalAccount;
import xyz.iknow.authenticaionserver.domain.account.repository.AccountRepository;
import xyz.iknow.authenticaionserver.security.customUserDetails.CustomUserDetails;
import xyz.iknow.authenticaionserver.security.jwt.service.JwtService;
import xyz.iknow.authenticaionserver.utility.redis.token.TokenService.TokenService;
import xyz.iknow.authenticaionserver.utility.validator.EmailValidator;

import java.util.Map;

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
         Boolean result = accountRepository.existsLocalAccountByEmail(email);
        return result;
    }

    @Override
    public ResponseEntity<Map> createAccount(AccountDTO request) {
        String email = request.getEmail();
        String password = request.getPassword();

        if (!emailValidator.validate(email)) {
            return ResponseEntity.badRequest().body(Map.of("message", "이메일 형식이 올바르지 않습니다."));
        }


        if (accountRepository.existsLocalAccountByEmail(email)) {
            return ResponseEntity.badRequest().body(Map.of("message", "이미 가입된 이메일입니다."));
        }

        LocalAccount account = LocalAccount.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .build();
        accountRepository.save(account);

        return ResponseEntity.ok().body(Map.of("message", "회원가입에 성공했습니다.", "status", "success"));
    }

    @Override
    public ResponseEntity<AccountDTO> getMyInfo() {
        Account account = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAccount();
        return ResponseEntity.ok(AccountDTO.builder()
                .id(account.getId())
                .nickname(account.getNickname())
                .build());
    }
}
