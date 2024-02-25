package xyz.iknow.authenticaionserver.test;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

@Component
public class AccountGenerator {
    static Long accoundId = 0L;
    public static String getTestEmail() {
        accoundId++;
        return "test" + accoundId + "@email.com";
    }
    static Long uniqueId = 0L;
    public static Long getUniqueId() {
        uniqueId++;
        return uniqueId;
    }
    public static String getTestPassword() {
        return RandomStringUtils.random(10, true, true)+accoundId;
    }
}
