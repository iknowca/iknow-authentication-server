package xyz.iknow.authenticaionserver.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.links.Link;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import xyz.iknow.authenticaionserver.docs.account.AccountRequest;
import xyz.iknow.authenticaionserver.docs.account.AccountResponse;

import java.util.Map;


@Configuration
@OpenAPIDefinition
@RequiredArgsConstructor
public class DocsConfig {

    @Bean
    public OpenAPI openAPI() {
        OpenAPI openAPI = new OpenAPI();
        openAPI.info(new Info().title("Authentication Server").description("Account 도메인과 인증을 담당하는 서버입니다.").version("1.0"));

        openAPI.setComponents(new Components());
        return openAPI;
    }
}
