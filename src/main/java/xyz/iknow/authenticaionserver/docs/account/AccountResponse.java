package xyz.iknow.authenticaionserver.docs.account;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import xyz.iknow.authenticaionserver.utility.response.MessageResponseBody;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AccountResponse {
    private final OpenAPI openAPI;
    @Bean
    public Schema joinSuccessResponse() {
        Schema response = new Schema<Map<String, Object>>()
                .type("object")
                .name("join response")
                .addProperty("message", new StringSchema().example("회원 가입 성공"))
                .addProperty("status", new StringSchema().example("success"));;

        openAPI.getComponents().addSchemas("joinSuccessResponse", response);
        return response;
    }
    @Bean
    public Schema emailDuplicated() {
        Schema response = new Schema<Map<String, Object>>()
                .type("object")
                .name("join request")
                .addProperty("message", new StringSchema().example("EMAIL_DUPLICATION"))
                .addProperty("status", new StringSchema().example("failure"));

        openAPI.getComponents().addSchemas("emailDuplicatedResponse", response);
        return response;
    }
    @Bean
    public Schema invalidEmail() {
        Schema response = new Schema<MessageResponseBody>()
                .type("object")
                .name("join request")
                .addProperty("message", new StringSchema().example("INVALID_EMAIL"))
                .addProperty("status", new StringSchema().example("failure"));

        openAPI.getComponents().addSchemas("invalidEmailResponse", response);
        return response;
    }
    @Bean
    public Schema userInfoResponse() {
        Schema accountDTO = new Schema<Map<String, Object>>()
                .type("object")
                .name("user info response")
                .addProperty("id", new StringSchema().example("1"))
                .addProperty("email", new StringSchema().example("test@gmail.com"))
                .addProperty("type", new StringSchema().example("local"))
                .addProperty("nickname", new StringSchema().example("nickname"))
                .addProperty("platform", new StringSchema().example("KAKAO"));

        Schema response = new Schema<Map<String, Object>>()
                .type("object")
                .name("user info response")
                .addProperty("data", accountDTO)
                .addProperty("status", new StringSchema().example("success"));


        openAPI.getComponents().addSchemas("userInfoResponse", response);
        return response;
    }
    @Bean Schema InvalidAccount() {
        Schema response = new Schema<MessageResponseBody>()
                .type("object")
                .name("invalid account")
                .addProperty("message", new StringSchema().example("INVALID_ACCOUNT"))
                .addProperty("status", new StringSchema().example("failure"));

        openAPI.getComponents().addSchemas("InvalidAccount", response);
        return response;
    }
    @Bean
    public Schema Unauthorized() {
        Schema response = new Schema<Map<String, Object>>()
                .type("object")
                .name("withdraw response")
                .addProperty("message", new StringSchema().example("Token is not found"))
                .addProperty("status", new StringSchema().example("false"));

        openAPI.getComponents().addSchemas("Unauthorized", response);
        return response;
    }
    @Bean
    public Schema InvalidUpdateRequest() {
        Schema response = new Schema<Map<String, Object>>()
                .type("object")
                .name("invalid update request")
                .addProperty("message", new StringSchema().example("INVALID_UPDATE_REQUEST"))
                .addProperty("status", new StringSchema().example("failure"));

        openAPI.getComponents().addSchemas("InvalidUpdateRequest", response);
        return response;
    }
    @Bean
    public Schema MessageResponse() {
        Schema response = new Schema<Map<String, Object>>()
                .type("object")
                .name("invalid email response")
                .addProperty("message", new StringSchema().example("message"))
                .addProperty("status", new StringSchema().example("success or failure"));

        openAPI.getComponents().addSchemas("MessageResponse", response);
        return response;
    }

    @Bean
    public Schema InvalidPlatform() {
        Schema response = new Schema<Map<String, Object>>()
                .type("object")
                .name("invalid platform")
                .addProperty("message", new StringSchema().example("INVALID_PLATFORM"))
                .addProperty("status", new StringSchema().example("failure"));

        openAPI.getComponents().addSchemas("InvalidPlatform", response);
        return response;
    }
    @Bean
    public Schema oauthLognUrlResponse() {
        Schema response = new Schema<Map<String, Object>>()
                .type("object")
                .name("oauth login url response")
                .addProperty("data", new StringSchema().example("https://kauth.kakao.com/oauth/authorize?client_id=...&redirect_uri=...&response_type=code"))
                .addProperty("status", new StringSchema().example("success"));

        openAPI.getComponents().addSchemas("oauthLognUrlResponse", response);
        return response;
    }
    @Bean
    public Schema oauthLoginResponse() {
        Schema response = new Schema<Map<String, Object>>()
                .type("object")
                .name("oauth login response")
                .addProperty("data", new StringSchema().example("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QGdtYWlsLmNvbSIsImlhdCI6MTYyMzUwNzIwMCwiZXhwIjoxNjIzNTA3MjAwfQ.72J9"))
                .addProperty("status", new StringSchema().example("success"));

        openAPI.getComponents().addSchemas("oauthLoginResponse", response);
        return response;
    }
}
