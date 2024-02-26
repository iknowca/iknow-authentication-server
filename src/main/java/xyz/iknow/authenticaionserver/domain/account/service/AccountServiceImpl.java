package xyz.iknow.authenticaionserver.domain.account.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import xyz.iknow.authenticaionserver.domain.account.entity.Account;
import xyz.iknow.authenticaionserver.domain.account.entity.AccountDetails;
import xyz.iknow.authenticaionserver.domain.account.entity.dto.AccountDTO;
import xyz.iknow.authenticaionserver.domain.account.entity.LocalAccount;
import xyz.iknow.authenticaionserver.domain.account.entity.dto.UpdateAccountForm;
import xyz.iknow.authenticaionserver.domain.account.entity.oauthAccount.OauthAccount;
import xyz.iknow.authenticaionserver.domain.account.entity.oauthAccount.OauthPlatformType;
import xyz.iknow.authenticaionserver.domain.account.repository.AccountRepository;
import xyz.iknow.authenticaionserver.domain.account.repository.oauth.OauthPlatformRepository;
import xyz.iknow.authenticaionserver.security.customUserDetails.CustomUserDetails;
import xyz.iknow.authenticaionserver.security.jwt.service.JwtService;
import xyz.iknow.authenticaionserver.utility.redis.token.TokenService.TokenService;
import xyz.iknow.authenticaionserver.utility.validator.EmailValidator;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    final private AccountRepository accountRepository;
    final private EmailValidator emailValidator;
    final private JwtService jwtService;
    final private TokenService tokenService;
    final private BCryptPasswordEncoder passwordEncoder;
    private final OauthPlatformRepository oauthPlatformRepository;

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

        AccountDetails accountDetails = new AccountDetails();
        account.setAccountDetails(accountDetails);
        accountRepository.save(account);

        return ResponseEntity.ok().body(Map.of("message", "회원가입에 성공했습니다.", "status", "success"));
    }

    @Override
    @Transactional
    public ResponseEntity<AccountDTO> getMyInfo() {
        Account account = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAccount();

        AccountDTO accountDTO = AccountDTO.builder()
                .id(account.getId())
                .nickname(account.getNickname())
                .accountType(account.getClass().getSimpleName())
                .build();
        if (account instanceof LocalAccount) {
            accountDTO.setEmail(((LocalAccount) account).getEmail());
        }
        if (account instanceof OauthAccount) {
            OauthPlatformType platformType = oauthPlatformRepository.findByAccountId(account.getId());
            accountDTO.setOauthPlatform(platformType.name());
        }

        return ResponseEntity.ok(accountDTO);
    }

    @Override
    public ResponseEntity<Map> updateMyInfo(UpdateAccountForm request) {
        Account account = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAccount();

        if (request.getPassword() == null && request.getNickname() == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "변경할 정보가 없습니다."));
        }

        if (request.getNickname() != null) {
            account.setNickname(request.getNickname());
        }
        if (request.getPassword() != null) {
            if (account instanceof LocalAccount) {
                ((LocalAccount) account).setPassword(passwordEncoder.encode(request.getPassword()));

            } else if (account instanceof OauthAccount) {
                return ResponseEntity.badRequest().body(Map.of("message", "소셜 로그인 사용자는 비밀번호를 변경할 수 없습니다."));
            }
        }
        accountRepository.save(account);
        AccountDTO accountDTO = AccountDTO.builder()
                .id(account.getId())
                .nickname(account.getNickname())
                .build();
        if (account instanceof LocalAccount) {
            accountDTO.setEmail(((LocalAccount) account).getEmail());
        }
        if (account instanceof OauthAccount) {
            OauthPlatformType platformType = oauthPlatformRepository.findByAccountId(account.getId());
            accountDTO.setOauthPlatform(platformType.name());
        }

        return ResponseEntity.ok().body(Map.of("message", "정보가 변경되었습니다.", "status", "success",
                "data", accountDTO));
    }

    @Override
    public ResponseEntity<Map> logout() {
        log.info("logout called");
        Account account = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAccount();
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletResponse response = sra.getResponse();

        tokenService.delete(account.getId());

        Cookie refreshToken = new Cookie("refreshToken", null);
        refreshToken.setMaxAge(0);
        refreshToken.setPath("/");

        response.addCookie(refreshToken);

        return ResponseEntity.ok().body(Map.of("message", "로그아웃 되었습니다.", "status", "success"));
    }

    @Override
    public ResponseEntity<Map> withdrawAccount() {
        log.info("withdrawAccount called");
        Account account = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAccount();
        account.getAccountDetails().setWithDraw(true);
        accountRepository.save(account);
        return ResponseEntity.ok().body(Map.of("message", "회원탈퇴 되었습니다.", "status", "success"));
    }
}
