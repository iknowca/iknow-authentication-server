package xyz.iknow.authenticaionserver.domain.account.controller;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import xyz.iknow.authenticaionserver.domain.account.dto.AccountDTO;
import xyz.iknow.authenticaionserver.domain.account.dto.LocalAccountDTO;
import xyz.iknow.authenticaionserver.domain.account.entity.Account;
import xyz.iknow.authenticaionserver.domain.account.service.AccountService;
import xyz.iknow.authenticaionserver.security.customUserDetails.CustomUserDetails;

import java.net.URI;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {
    final private AccountService accountService;

    @PostMapping("/validate-email")
    public Boolean validateEmail(@RequestBody Map request) {
        return accountService.validateEamil((String) request.get("email"));
    }

    @PostMapping("/join")
    public ResponseEntity join(@RequestBody LocalAccountDTO request) {
        accountService.createAccount(request);
        return ResponseEntity.created(URI.create("/account/login")).body(new Result(null, "success"));
    }

    @GetMapping
    public ResponseEntity<Result<AccountDTO>> getMyInfo() {
        Account account = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAccount();
        AccountDTO accountDTO = accountService.getMyInfo(account);
        return ResponseEntity.ok(new Result<>(accountDTO, "success"));
    }

    @PutMapping
    public ResponseEntity<Result<AccountDTO>> updateMyInfo(@RequestBody AccountDTO request) {
        Account account = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAccount();
        AccountDTO accountDTO = accountService.updateMyInfo(account, request);
        return ResponseEntity.ok(new Result<>(accountDTO, "success"));
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Result> logout() {
        Account account = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAccount();
        accountService.logout(account);
        return ResponseEntity.ok(new Result(null, "success"));
    }

    @DeleteMapping
    public ResponseEntity<Result> withdrawAccount() {
        Account account = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAccount();
        accountService.withdrawAccount(account);
        return ResponseEntity.status(HttpStatus.GONE).body(new Result(null, "success"));
    }


    @AllArgsConstructor
    @Getter
    public class Result<T> {
        private T data;
        private String status;
    }
}
