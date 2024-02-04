package xyz.iknow.authenticaionserver.account.apiTest;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import xyz.iknow.authenticaionserver.domain.account.entity.Account;
import xyz.iknow.authenticaionserver.domain.account.entity.AccountDTO;
import xyz.iknow.authenticaionserver.domain.account.repository.AccountRepository;
import xyz.iknow.authenticaionserver.utility.redis.token.TokenRepository.AccessTokenRepository;
import xyz.iknow.authenticaionserver.utility.redis.token.TokenRepository.RefreshTokenRepository;

import java.util.Optional;

import static org.mockito.Mockito.*;
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

    @Test
    @DisplayName("로그인 성공")
    public void testLogin() throws Exception {
        //given
        String email = "test@example.com";
        String password = "test1234";
        Long id = 1L;
        when(accountRepository.findByEmailAndPassword(email, password)).thenReturn(Optional.of(Account.builder().id(1L).email(email).password(password).build()));
        when(accessTokenRepository.save(any())).thenReturn(null);
        when(refreshTokenRepository.save(any())).thenReturn(null);

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/account/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(AccountDTO.builder().email(email).password(password).build())));
        //then
        verify(accountRepository, times(1)).findByEmailAndPassword(email, password);
        verify(accessTokenRepository, times(1)).save(any());
        verify(refreshTokenRepository, times(1)).save(any());
        resultActions.andExpect(jsonPath("$.status").value("success"))
                .andExpectAll(jsonPath("$.accessToken").exists(),
                        jsonPath("$.refreshToken").exists())
                .andDo(mvcResult -> System.out.println(mvcResult.getResponse().getContentAsString()));
    }
    @Test
    @DisplayName("로그인 실패 - 존재하지 않는 이메일")
    public void testLogin2() throws Exception {
        //given
        String email = "test@example.com";
        String password = "test1234";
        when(accountRepository.findByEmailAndPassword(email, password)).thenReturn(Optional.empty());

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/account/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(AccountDTO.builder().email(email).password(password).build())));

        //then
        verify(accountRepository, times(1)).findByEmailAndPassword(email, password);
        resultActions.andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("로그인에 실패하였습니다."));
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 불일치")
    public void testLogin3() throws Exception {
        //given
        String email = "test@example.com";
        String password = "test1234";
        when(accountRepository.findByEmailAndPassword(email, password)).thenReturn(Optional.empty());

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/account/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(AccountDTO.builder().email(email).password(password).build())));

        //then
        verify(accountRepository, times(1)).findByEmailAndPassword(email, password);
        resultActions.andExpect(jsonPath("$.status").value("fail"))
                .andExpect(jsonPath("$.message").value("로그인에 실패하였습니다."));
    }
}
