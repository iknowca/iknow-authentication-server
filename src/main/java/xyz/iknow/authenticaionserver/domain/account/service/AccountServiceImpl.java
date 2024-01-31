package xyz.iknow.authenticaionserver.domain.account.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import xyz.iknow.authenticaionserver.domain.account.entity.Account;
import xyz.iknow.authenticaionserver.domain.account.entity.AccountDTO;
import xyz.iknow.authenticaionserver.domain.account.repository.AccountRepository;
import xyz.iknow.authenticaionserver.utility.validator.EmailValidator;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    final private AccountRepository accountRepository;
    final private EmailValidator emailValidator;

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
                .password(password)
                .build();
        accountRepository.save(account);

        return ResponseEntity.ok().body(Map.of("message", "회원가입에 성공했습니다.", "status", "success"));
    }

    @Override
    public ResponseEntity<Map> login(AccountDTO request) {
        String email = request.getEmail();
        String password = request.getPassword();

        Optional<Account> maybeAccount = accountRepository.findByEmailAndPassword(email, password);
        if (maybeAccount.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("message", "로그인에 실패하였습니다.", "status", "fail"));
        }
        Account account = maybeAccount.get();
        System.out.println(account.getId());
        return ResponseEntity.ok(Map.of("status", "success",
                "accessToken", "Bearer accessToken"+account.getId(),
                "refreshToken", "Bearer refreshToken"+account.getId()));
    }
}
