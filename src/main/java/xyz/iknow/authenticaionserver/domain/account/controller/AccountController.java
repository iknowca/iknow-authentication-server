package xyz.iknow.authenticaionserver.domain.account.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.iknow.authenticaionserver.domain.account.entity.AccountDTO;
import xyz.iknow.authenticaionserver.domain.account.service.AccountService;

import java.util.Map;

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
    public ResponseEntity<Map> join(@RequestBody AccountDTO request) {
        return accountService.createAccount(request);
    }
//    @PostMapping("/login")
//    public ResponseEntity<Map> login(@RequestBody AccountDTO request) {
//        return accountService.login(request);
//    }
    @PostMapping("/refresh")
    public ResponseEntity<Map> refresh(@RequestBody Map request) {
        return accountService.refresh(request);
    }

    @GetMapping("/my-info")
    public ResponseEntity<AccountDTO> getMyInfo(@RequestHeader("Authorization") String token) {
        return accountService.getMyInfo(token);
}
}
