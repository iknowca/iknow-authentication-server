package xyz.iknow.authenticaionserver.domain.account.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.iknow.authenticaionserver.domain.account.entity.dto.AccountDTO;
import xyz.iknow.authenticaionserver.domain.account.entity.dto.UpdateAccountForm;
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

    @GetMapping
    public ResponseEntity<AccountDTO> getMyInfo() {
        return accountService.getMyInfo();
}
    @PatchMapping
    public ResponseEntity<Map> updateMyInfo(@RequestBody UpdateAccountForm request) {
        return accountService.updateMyInfo(request);
    }
    @DeleteMapping("/logout")
    public ResponseEntity<Map> logout() {
        return accountService.logout();
    }
    @DeleteMapping
    public ResponseEntity<Map> withdrawAccount() {
        return accountService.withdrawAccount();
    }
}
