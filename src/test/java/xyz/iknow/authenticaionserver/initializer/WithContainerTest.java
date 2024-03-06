package xyz.iknow.authenticaionserver.initializer;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MySQLContainer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@RequiredArgsConstructor
public class WithContainerTest {
    private static final String REDIS_IMAGE = "redis:7.0.8-alpine";
    private static final int REDIS_PORT = 6379;
    private static final String REDIS_PASSWORD = "test";

    protected static GenericContainer redisContainer = new GenericContainer(REDIS_IMAGE)
            .withExposedPorts(REDIS_PORT)
            .withEnv("REDIS_PASSWORD", REDIS_PASSWORD);

    static {
        redisContainer.start();
        System.setProperty("spring.data.redis.host", redisContainer.getHost());
        System.setProperty("spring.data.redis.password", String.valueOf(redisContainer.getEnvMap().get("REDIS_PASSWORD")));
        System.setProperty("spring.data.redis.port", String.valueOf(redisContainer.getMappedPort(REDIS_PORT)));

        System.setProperty("spring.data.redis.cache.host", redisContainer.getHost());
        System.setProperty("spring.data.redis.cache.password", String.valueOf(redisContainer.getEnvMap().get("REDIS_PASSWORD")));
        System.setProperty("spring.data.redis.cache.port", String.valueOf(redisContainer.getMappedPort(REDIS_PORT)));
    }
}
