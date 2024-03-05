package xyz.iknow.authenticaionserver.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AccountGenerator {
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
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public String getTestPassword() {
        return RandomStringUtils.random(10, true, true) + accoundId;
    }

    public String getTestNickname() {
        return "무명회원#" + RandomStringUtils.random(5);
    }
}
