package xyz.iknow.authenticaionserver.domain.account.controller.oauth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.iknow.authenticaionserver.domain.account.service.oauth.OauthAccountService;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/account/oauth")
public class OauthAccountController {
    private final OauthAccountService oauthAccountService;

    @GetMapping("/url/{platform}")
    public ResponseEntity<Map> getOauthUrl(@PathVariable String platform) {
        return oauthAccountService.getOauthUrl(platform);
    }
    @GetMapping("/callback/{platform}")
    public ResponseEntity<Map> login(@PathVariable String platform,@RequestParam String code) {
        return oauthAccountService.login(platform, code);
    }
}
