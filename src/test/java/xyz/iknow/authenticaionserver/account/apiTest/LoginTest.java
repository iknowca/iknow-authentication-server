package xyz.iknow.authenticaionserver.account.apiTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import xyz.iknow.authenticaionserver.domain.account.entity.Account;
import xyz.iknow.authenticaionserver.domain.account.entity.AccountDTO;
import xyz.iknow.authenticaionserver.domain.account.repository.AccountRepository;
import xyz.iknow.authenticaionserver.domain.jwt.service.JwtService;
import xyz.iknow.authenticaionserver.utility.redis.token.TokenRepository.AccessTokenRepository;
import xyz.iknow.authenticaionserver.utility.redis.token.TokenRepository.RefreshTokenRepository;

import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("로그인 테스트")
public class LoginTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AccountRepository accountRepository;
    @MockBean
    private AccessTokenRepository accessTokenRepository;
    @MockBean
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @MockBean
    private JwtService jwtService;

    @Test
    @DisplayName("로그인 성공")
    public void testLogin() throws Exception {
        //given
        String email = "test2@email.com";
        String password = "1234";
        System.out.println(password);

        String accessToken = "validAccessToken";
        String refreshToken = "validRefreshToken";


        Long id = 2L;
        when(accountRepository.findByEmail(email)).thenReturn(Optional.of(Account.builder().id(id).email(email).password(passwordEncoder.encode(password)).build()));
        when(jwtService.generateRefreshToken(any())).thenReturn(refreshToken);
        when(jwtService.generateAccessToken(any())).thenReturn(accessToken);

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/account/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(Map.of("email", email, "password", password))));
        //then
        verify(accountRepository, times(1)).findByEmail(email);
        verify(jwtService, times(1)).generateAccessToken(any());
        verify(jwtService, times(1)).generateRefreshToken(any());
        resultActions.andExpectAll(jsonPath("$.accessToken").exists());
        resultActions.andExpect(cookie().value("refreshToken", "validRefreshToken"));
    }
    @Test
    @DisplayName("로그인 실패 - 존재하지 않는 이메일")
    public void testLogin2() throws Exception {
        //given
        String email = "test@example.com";
        String invalidEmail = "invlid@test.com";
        String password = "test1234";
        when(accountRepository.findByEmail(invalidEmail)).thenReturn(Optional.empty());

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/account/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(AccountDTO.builder().email(invalidEmail).password(passwordEncoder.encode(password)).build())));

        //then
        verify(accountRepository, times(2)).findByEmail(invalidEmail);
        resultActions.andExpect(jsonPath("$.status").value("Bad credentials"));
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 불일치")
    public void testLogin3() throws Exception {
        //given
        String email = "test@example.com";
        String password = "test1234";
        when(accountRepository.findByEmail(email)).thenReturn(Optional.empty());

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/account/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(AccountDTO.builder().email(email).password(passwordEncoder.encode("invalidPassword")).build())));

        //then
        verify(accountRepository, times(2)).findByEmail(email);
        resultActions.andExpect(jsonPath("$.status").value("Bad credentials"));
    }
}
