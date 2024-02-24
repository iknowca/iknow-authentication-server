package xyz.iknow.authenticaionserver.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
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
import xyz.iknow.authenticaionserver.initializer.RedisInitializer;
import xyz.iknow.authenticaionserver.security.jwt.service.JwtService;
import xyz.iknow.authenticaionserver.utility.redis.token.TokenRepository.AccessTokenRepository;
import xyz.iknow.authenticaionserver.utility.redis.token.TokenRepository.RefreshTokenRepository;

@SpringBootTest
@ExtendWith(RedisInitializer.class)
@AutoConfigureMockMvc
public class MockMvcTest {
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
    protected JwtService jwtService;
    @MockBean
    protected RestTemplate restTemplate;

    static Long accoundId = 0L;
    public String getTestEmail() {
        accoundId++;
        return "test" + accoundId + "@email.com";
    }
    static Long uniqueId = 0L;
    public Long getUniqueId() {
        uniqueId++;
        return uniqueId;
    }
    public String getTestPassword() {
        return RandomStringUtils.random(10, true, true)+accoundId;
    }
}
