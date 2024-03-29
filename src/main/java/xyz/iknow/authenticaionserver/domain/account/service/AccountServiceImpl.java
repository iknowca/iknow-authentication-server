package xyz.iknow.authenticaionserver.domain.account.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import xyz.iknow.authenticaionserver.domain.account.dto.AccountDTO;
import xyz.iknow.authenticaionserver.domain.account.dto.LocalAccountDTO;
import xyz.iknow.authenticaionserver.domain.account.dto.oauth.OauthAccountDTO;
import xyz.iknow.authenticaionserver.domain.account.dto.oauth.OauthPlatformDTO;
import xyz.iknow.authenticaionserver.domain.account.entity.Account;
import xyz.iknow.authenticaionserver.domain.account.entity.AccountDetails;
import xyz.iknow.authenticaionserver.domain.account.entity.LocalAccount;
import xyz.iknow.authenticaionserver.domain.account.entity.oauthAccount.OauthAccount;
import xyz.iknow.authenticaionserver.domain.account.exception.AccountException;
import xyz.iknow.authenticaionserver.domain.account.repository.AccountRepository;
import xyz.iknow.authenticaionserver.domain.account.repository.oauth.OauthPlatformRepository;
import xyz.iknow.authenticaionserver.security.jwt.service.JwtService;
import xyz.iknow.authenticaionserver.utility.redis.token.TokenService.TokenService;
import xyz.iknow.authenticaionserver.utility.validator.EmailValidator;

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
        if (!emailValidator.validate(email)) {
            throw new AccountException(AccountException.ACCOUNT_ERROR.INVALID_EMAIL);
        }
        if (accountRepository.existsLocalAccountByEmail(email)) {
            return false;
        }
        return true;
    }

    @Override
    public void createAccount(LocalAccountDTO request) {
        String email = request.getEmail();
        String password = request.getPassword();

        if (!emailValidator.validate(email)) {
            throw new AccountException(AccountException.ACCOUNT_ERROR.INVALID_EMAIL);
        }
        if (accountRepository.existsLocalAccountByEmail(email)) {
            throw new AccountException(AccountException.ACCOUNT_ERROR.EMAIL_DUPLICATION);
        }


        LocalAccount account = LocalAccount.builder().email(email).password(passwordEncoder.encode(password)).nickname("무명회원#" + RandomStringUtils.random(4)).build();

        AccountDetails accountDetails = new AccountDetails();
        account.setAccountDetails(accountDetails);
        accountRepository.save(account);
    }

    @Override
    @Transactional
    public AccountDTO getMyInfo(Long accountId) {

        Account account = accountRepository.findById(accountId).orElseThrow(() -> new AccountException(AccountException.ACCOUNT_ERROR.INVALID_ACCOUNT));
        AccountDTO accountDTO;
        if (account instanceof LocalAccount) {
            accountDTO = new LocalAccountDTO((LocalAccount) account);
            accountDTO.setType("local");
        } else if (account instanceof OauthAccount) {
            accountDTO = new OauthAccountDTO((OauthAccount) account);
            accountDTO.setType("oauth");
            OauthPlatformDTO oauthPlatformDTO = new OauthPlatformDTO(accountRepository.findOauthPlatformByPlatformTypeAndOauthId(account.getId()));
            ((OauthAccountDTO) accountDTO).setOauthPlatform(oauthPlatformDTO);
        } else {
            throw new AccountException(AccountException.ACCOUNT_ERROR.INVALID_ACCOUNT);
        }

        return accountDTO;
    }

    @Override
    public AccountDTO updateMyInfo(Long accountId, AccountDTO request) {
        if (request.getNickname() == null) {
            throw new AccountException(AccountException.ACCOUNT_ERROR.INVALID_UPDATE_REQUEST);
        } else {
            accountRepository.updateNicknameById(request.getNickname(), accountId);
            return getMyInfo(accountId);
        }
    }

    @Override
    public void logout(Long accountId) {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletResponse response = sra.getResponse();

        tokenService.delete(accountId);

        Cookie refreshToken = new Cookie("refreshToken", null);
        refreshToken.setMaxAge(0);
        refreshToken.setPath("/");

        response.addCookie(refreshToken);
    }

    @Override
    public void withdrawAccount(Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new AccountException(AccountException.ACCOUNT_ERROR.INVALID_ACCOUNT));
        account.getAccountDetails().setWithDraw(true);
        accountRepository.save(account);
        logout(accountId);
    }

    @Override
    public AccountDTO changePassword(Long accountId, LocalAccountDTO request) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new AccountException(AccountException.ACCOUNT_ERROR.INVALID_ACCOUNT));
        String password = request.getPassword();

        if (!(account instanceof LocalAccount)) {
            throw new AccountException(AccountException.ACCOUNT_ERROR.SOCIAL_ACCOUNT_DID_NOT_UPDATE_PASSWORD);
        }

        ((LocalAccount) account).setPassword(passwordEncoder.encode(password));
        account = accountRepository.save(account);
        LocalAccountDTO localAccountDTO = new LocalAccountDTO((LocalAccount) account);
        return localAccountDTO;
    }

    @Override
    public String login(LocalAccountDTO request) {
        String email = request.getEmail();
        String password = request.getPassword();

        if (!emailValidator.validate(email)) {
            throw new AccountException(AccountException.ACCOUNT_ERROR.INVALID_ACCOUNT);
        }

        LocalAccount account = accountRepository.findLocalAccountByEmail(email).orElseThrow(() -> new AccountException(AccountException.ACCOUNT_ERROR.INVALID_ACCOUNT));

        if (!passwordEncoder.matches(password, account.getPassword())) {
            throw new AccountException(AccountException.ACCOUNT_ERROR.INVALID_ACCOUNT);
        }
        if (account.getAccountDetails().getWithDraw()) {
            throw new AccountException(AccountException.ACCOUNT_ERROR.INVALID_ACCOUNT);
        }

        String accessToken = jwtService.generateAccessToken(account);
        String refreshToken = jwtService.generateRefreshToken(account);

        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletResponse response = sra.getResponse();

        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setMaxAge(60 * 60 * 24);
        refreshTokenCookie.setPath("/");

        response.addCookie(refreshTokenCookie);
        return "Bearer " + accessToken;
    }
}
