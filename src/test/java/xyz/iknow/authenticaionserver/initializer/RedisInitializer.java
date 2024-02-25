package xyz.iknow.authenticaionserver.initializer;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

@Component
@Profile("ci-test")
public class RedisInitializer implements BeforeAllCallback {
    private static final String REDIS_IMAGE = "redis:7.0.8-alpine";
    private static final int REDIS_PORT = 6379;
    private static final String REDIS_PASSWORD = "test";
    private static GenericContainer redis;

    @Override
    public void beforeAll(ExtensionContext context) {
        redis = new GenericContainer(DockerImageName.parse(REDIS_IMAGE))
                .withExposedPorts(REDIS_PORT)
                .withEnv("REDIS_PASSWORD", REDIS_PASSWORD);
        redis.start();
        System.setProperty("spring.data.redis.host", redis.getHost());
        System.setProperty("spring.data.redis.password", String.valueOf(redis.getEnvMap().get("REDIS_PASSWORD")));
        System.setProperty("spring.data.redis.port", String.valueOf(redis.getMappedPort(REDIS_PORT)));

        System.setProperty("spring.data.redis.cache.host", redis.getHost());
        System.setProperty("spring.data.redis.cache.password", String.valueOf(redis.getEnvMap().get("REDIS_PASSWORD")));
        System.setProperty("spring.data.redis.cache.port", String.valueOf(redis.getMappedPort(REDIS_PORT)));
    }
}
