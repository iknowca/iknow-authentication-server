package xyz.iknow.authenticaionserver.domain.account.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
}
