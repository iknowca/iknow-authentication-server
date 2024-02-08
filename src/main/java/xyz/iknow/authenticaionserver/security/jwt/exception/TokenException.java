package xyz.iknow.authenticaionserver.security.jwt.exception;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.IOException;
import java.util.Map;

public class TokenException extends RuntimeException {
    TOKEN_ERROR tokenError;

    @Getter
    @AllArgsConstructor
    public enum TOKEN_ERROR {
        EXPIRED_TOKEN(401, "Token is expired"),
        INVALID_TOKEN(401, "Token is invalid"),
        NOT_FOUND_TOKEN(401, "Token is not found"),
        MALFORMED_TOKEN(401, "Token is malformed");

        private final int status;
        private final String message;
    }

    public TokenException(TOKEN_ERROR tokenError) {
        super(tokenError.name());
        this.tokenError = tokenError;
    }

    public void sendResponseError(HttpServletResponse response) throws IOException {
        response.setStatus(tokenError.getStatus());
        response.setContentType("application/json");
        Gson gson = new Gson();
        String responseStr = gson.toJson(Map.of("message", tokenError.getMessage()));

        response.getWriter().write(responseStr);
    }
}
