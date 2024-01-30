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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("회원가입 api 테스트")
public class JoinTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountRepository accountRepository;

    @Test
    @DisplayName("회원가입 성공")
    public void testJoin() throws Exception {
        //given
        String email = "test@example.com";
        String password = "test1234";

        when(accountRepository.existsByEmail(email)).thenReturn(false);
        when(accountRepository.save(Account.builder().email(email).password(password).build())).thenReturn(Account.builder().email(email).password(password).build());

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/account/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(AccountDTO.builder().email(email).password(password).build())));

        //then
        verify(accountRepository, times(1)).existsByEmail(email);
        resultActions.andExpect(jsonPath("$.status").value("success"));
    }
    @Test
    @DisplayName("회원가입 실패 - 이미 가입된 이메일")
    public void testJoin2() throws Exception {
        //given
        String email = "test@example.com";
        String password = "test1234";

        when(accountRepository.existsByEmail(email)).thenReturn(true);
        when(accountRepository.save(Account.builder().email(email).password(password).build())).thenReturn(Account.builder().email(email).password(password).build());

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/account/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(AccountDTO.builder().email(email).password(password).build())));

        //then
        verify(accountRepository, times(1)).existsByEmail(email);
        resultActions.andExpect(jsonPath("$.message").value("이미 가입된 이메일입니다."));
    }

//    TODO: 회원가입 제약 사항 추후 구현
//    @Test
//    @DisplayName("회원가입 실패 - 이메일 형식이 아님")
//
//
//    @Test
//    @DisplayName("회원가입 실패 - 비밀번호 형식이 아님")
}
