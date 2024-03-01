package xyz.iknow.authenticaionserver;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.iknow.authenticaionserver.initializer.RedisInitializer;
import xyz.iknow.authenticaionserver.test.IntegrationTest;

@SpringBootTest
@ExtendWith(RedisInitializer.class)
class AuthenticaionserverApplicationTests extends IntegrationTest {

    @Test
    void contextLoads() {
    }

}
