package xyz.iknow.authenticaionserver.domain.account.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AccountExceptionHandler {
    @ExceptionHandler(AccountException.class)
    public ResponseEntity<String> handler(AccountException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
