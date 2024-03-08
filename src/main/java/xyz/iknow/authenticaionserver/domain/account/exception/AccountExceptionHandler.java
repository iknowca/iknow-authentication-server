package xyz.iknow.authenticaionserver.domain.account.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import xyz.iknow.authenticaionserver.utility.response.MessageResponseBody;

@RestControllerAdvice
public class AccountExceptionHandler {
    @ExceptionHandler(AccountException.class)
    public ResponseEntity<MessageResponseBody> handler(AccountException e) {
        return ResponseEntity.status(e.error.getStatus()).body(new MessageResponseBody(e.getMessage(), "failure"));
    }
}
