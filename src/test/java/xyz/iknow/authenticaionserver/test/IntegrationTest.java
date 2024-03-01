package xyz.iknow.authenticaionserver.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;
import xyz.iknow.authenticaionserver.domain.account.repository.AccountRepository;
import xyz.iknow.authenticaionserver.domain.account.repository.oauth.OauthPlatformRepository;
import xyz.iknow.authenticaionserver.domain.account.service.AccountService;
import xyz.iknow.authenticaionserver.domain.account.service.oauth.OauthAccountService;
import xyz.iknow.authenticaionserver.security.jwt.service.JwtService;
import xyz.iknow.authenticaionserver.utility.redis.token.TokenRepository.AccessTokenRepository;
import xyz.iknow.authenticaionserver.utility.redis.token.TokenRepository.RefreshTokenRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTest {
    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;
    @SpyBean
    @Autowired
    protected AccountRepository accountRepository;
    @Autowired
    protected AccessTokenRepository accessTokenRepository;
    @Autowired
    protected RefreshTokenRepository refreshTokenRepository;
    @Autowired
    protected OauthPlatformRepository oauthPlatformRepository;
    @Autowired
    protected BCryptPasswordEncoder passwordEncoder;
    @Autowired
    protected AccountService accountService;
    @Autowired
    protected JwtService jwtService;
    @MockBean
    protected RestTemplate restTemplate;
    @Autowired
    protected AccountGenerator ag;
    @Autowired
    protected OauthAccountService oauthAccountService;
}
