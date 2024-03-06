package xyz.iknow.authenticaionserver.docs.account;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import jakarta.annotation.Priority;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Getter
@RequiredArgsConstructor
public class AccountRequest {
    private final OpenAPI openAPI;
    @Bean
    public Schema JoinAccount() {
        Schema request = new Schema<Map<String, Object>>()
                .type("object")
                .contentMediaType("application/json")
                .name("join request")
                .addProperty("email", new StringSchema().example("test@gmail.com"))
                .addProperty("password", new StringSchema().example("P4SSW0RD"));
        request.setRequired(List.of("email", "password"));

        openAPI.getComponents().addSchemas("JoinAccountRequest", request);
        return request;
    }
    @Bean
    public Schema updateAccountRequest() {
        Schema request = new Schema<Map<String, Object>>()
                .type("object")
                .contentMediaType("application/json")
                .name("update request")
                .addProperty("type", new StringSchema().example("local").description("계정 타입"))
                .addProperty("nickname", new StringSchema().example("John")).description("닉네임");
        request.setRequired(List.of("type"));

        openAPI.getComponents().addSchemas("UpdateAccountRequest", request);
        return request;
    }
    @Bean
    public Schema validateEmailRequest() {
        Schema request = new Schema<Map<String, Object>>()
                .type("object")
                .contentMediaType("application/json")
                .name("validate email request")
                .addProperty("type", new StringSchema().example("local"))
                .addProperty("email", new StringSchema().example("test@gmail.com"));
        request.setRequired(List.of("type", "email"));

        openAPI.getComponents().addSchemas("validateEmailRequest", request);
        return request;
    }
    @Bean
    public Schema updatePassword() {
        Schema request = new Schema<Map<String, Object>>()
                .type("object")
                .contentMediaType("application/json")
                .name("update password request")
                .addProperty("type", new StringSchema().example("local").description("계정 타입"))
                .addProperty("password", new StringSchema().example("password")).description("패스워드");
        request.setRequired(List.of("type"));

        openAPI.getComponents().addSchemas("UpdateAccountPasswordRequest", request);
        return request;
    }
    @Bean
    public Schema loginRequest() {
        Schema request = new Schema<Map<String, Object>>()
                .type("object")
                .contentMediaType("application/json")
                .name("login request")
                .addProperty("type", new StringSchema().example("local").description("계정 타입"))
                .addProperty("email", new StringSchema().example("test@email.com").description("이메일"))
                .addProperty("password", new StringSchema().example("password").description("패스워드"));
        request.setRequired(List.of("type", "email", "password"));
        openAPI.getComponents().addSchemas("LoginRequest", request);
        return request;
    }
}
