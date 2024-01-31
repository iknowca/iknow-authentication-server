package xyz.iknow.authenticaionserver.utility.validator;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@WebMvcTest(EmailValidator.class)
@DisplayName("이메일 유효성 검사 테스트")
public class EmailValidatorTest {

    @Autowired
    private EmailValidator emailValidator;
    @Test
    @DisplayName("이메일 유효성 검사 성공")
    public void testValidate() {
        //given
        String email = "suitableEmail@test.com";

        //when
        Boolean validation = emailValidator.validate(email);

        //then
        assertTrue(validation);
    }

    @Test
    @DisplayName("이메일 유효성 검사 실패")
    public void testValidate2() {
        //given
        String email = "unsuitableEmail";

        //when
        Boolean validation = emailValidator.validate(email);

        //then
        assertFalse(validation);
    }
}
