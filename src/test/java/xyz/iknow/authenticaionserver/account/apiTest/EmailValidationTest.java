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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import xyz.iknow.authenticaionserver.domain.account.repository.AccountRepository;

import java.util.Map;

import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("이메일 계정 확인 테스트")
public class EmailValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountRepository accountRepository;

    @Test
    @DisplayName("해당 이메일로 가입된 계정이 존재하는 경우")
    public void testValidateEmail() throws Exception {
        //given
        String email = "test@example.com";
        when(accountRepository.existsByEmail(email)).thenReturn(true);

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/account/validate-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("email", email))));
        //then
        verify(accountRepository, times(1)).existsByEmail(email);
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("true"));
    }

    @Test
    @DisplayName("해당 이메일로 가입된 계정이 존재하지 않는 경우")
    public void testValidateEmail2() throws Exception {
        //given
        String email = "test@example.com";
        when(accountRepository.existsByEmail(email)).thenReturn(false);

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/account/validate-email")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("email", email))));
        //then
        verify(accountRepository, times(1)).existsByEmail(email);
        resultActions.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("false"));
    }
}