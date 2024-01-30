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

    @Test
    @DisplayName("로그인 성공")
    public void testLogin() throws Exception {
        //given
        String email = "test@example.com";
        String password = "test1234";
        Long id = 1L;
        when(accountRepository.findByEmailAndPassword(email, password)).thenReturn(Optional.of(Account.builder().id(1L).email(email).password(password).build()));

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/account/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(AccountDTO.builder().email(email).password(password).build())));
        //then
        verify(accountRepository, times(1)).findByEmailAndPassword(email, password);
        resultActions.andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.accessToken").value("Bearer accessToken"+1L))
                .andExpect(jsonPath("$.refreshToken").value("Bearer refreshToken"+1L));
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
