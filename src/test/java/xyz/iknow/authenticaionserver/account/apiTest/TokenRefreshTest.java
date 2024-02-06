package xyz.iknow.authenticaionserver.account.apiTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import xyz.iknow.authenticaionserver.domain.account.entity.Account;
import xyz.iknow.authenticaionserver.domain.account.repository.AccountRepository;
import xyz.iknow.authenticaionserver.domain.account.service.AccountService;
import xyz.iknow.authenticaionserver.domain.jwt.service.JwtService;
import xyz.iknow.authenticaionserver.utility.redis.token.TokenService.TokenService;
import xyz.iknow.authenticaionserver.utility.redis.token.token.RefreshToken;

import java.util.Map;
import java.util.Optional;

import static org.mockito.Mockito.*;


@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("토큰 리프레쉬 테스트")
public class TokenRefreshTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private AccountService accountService;
    @MockBean
    private JwtService jwtservice;
    @MockBean
    private TokenService tokenService;
    @MockBean
    private AccountRepository accountRepository;

    @Test
    @DisplayName("토큰 리프레쉬 성공")
    public void testRefreshToken() throws Exception {
        //given
        String refreshToken = "validRefreshToken";
        Long id = 1L;
        String email = "validEmail";
        Account account = Account.builder().id(id).email(email).build();
        String accessToken = "validAccessToken";

        when(jwtservice.parseToken(refreshToken)).thenReturn(Map.of("accountId", id.intValue()));
        when(tokenService.findRefreshTokenById(1L)).thenReturn(Optional.of(new RefreshToken(id, refreshToken, 1000L)));
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(jwtservice.generateAccessToken(account)).thenReturn("validAccessToken");
        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/account/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .cookie(new Cookie("refreshToken", refreshToken)));
        //then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken").value("Bearer validAccessToken"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("success"));
    }
}
