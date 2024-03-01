package xyz.iknow.authenticaionserver.domain.account.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
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
        Boolean result = accountRepository.existsLocalAccountByEmail(email);
        return result;
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
    public AccountDTO getMyInfo(Account account) {

        AccountDTO accountDTO;
        if (account instanceof LocalAccount) {
            accountDTO = new LocalAccountDTO((LocalAccount) account);
        } else if (account instanceof OauthAccount) {
            accountDTO = new OauthAccountDTO((OauthAccount) account);

            OauthPlatformDTO oauthPlatformDTO = new OauthPlatformDTO(accountRepository.findOauthPlatformByPlatformTypeAndOauthId(account.getId()));
            ((OauthAccountDTO) accountDTO).setOauthPlatform(oauthPlatformDTO);
        } else {
            throw new AccountException(AccountException.ACCOUNT_ERROR.INVALID_ACCOUNT);
        }

        return accountDTO;
    }

    @Override
    @Transactional
    public AccountDTO updateMyInfo(Account account, AccountDTO request) {
        if (account instanceof LocalAccount && request instanceof LocalAccountDTO) {
            updateLocalAccountInfo((LocalAccount) account, (LocalAccountDTO) request);
        } else if (account instanceof OauthAccount && request instanceof OauthAccountDTO) {
            updateOauthAccountInfo((OauthAccount) account, (OauthAccountDTO) request);
        } else {
            throw new AccountException(AccountException.ACCOUNT_ERROR.INVALID_UPDATE_REQUEST);
        }
        if (request.getNickname() != null) {
            account.setNickname(request.getNickname());
        }
        account = accountRepository.save(account);
        return getMyInfo(account);
    }

    private void updateLocalAccountInfo(LocalAccount account, LocalAccountDTO request) {
        if (request.getPassword() == null && request.getNickname() == null) {
            throw new AccountException(AccountException.ACCOUNT_ERROR.INVALID_UPDATE_REQUEST);
        } else if (request.getPassword() != null) {
            account.setPassword(passwordEncoder.encode(request.getPassword()));
        }
    }

    private void updateOauthAccountInfo(OauthAccount account, OauthAccountDTO request) {
        if (request.getNickname() == null) {
            throw new AccountException(AccountException.ACCOUNT_ERROR.INVALID_UPDATE_REQUEST);
        }
    }

    @Override
    public void logout(Account account) {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletResponse response = sra.getResponse();

        tokenService.delete(account.getId());

        Cookie refreshToken = new Cookie("refreshToken", null);
        refreshToken.setMaxAge(0);
        refreshToken.setPath("/");

        response.addCookie(refreshToken);
    }

    @Override
    public void withdrawAccount(Account account) {
        account.getAccountDetails().setWithDraw(true);
        accountRepository.save(account);
        logout(account);
    }
}
