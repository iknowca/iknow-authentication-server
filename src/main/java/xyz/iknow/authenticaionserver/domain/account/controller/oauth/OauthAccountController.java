package xyz.iknow.authenticaionserver.domain.account.controller.oauth;

import lombok.Getter;
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
    public ResponseEntity<Result<String>> getOauthUrl(@PathVariable(required = false) String platform) {
        String loginUrl = oauthAccountService.getOauthUrl(platform);
        return ResponseEntity.ok(new Result<>(loginUrl, "success"));
    }

    @GetMapping("/callback/{platform}")
    public ResponseEntity<Map> login(@PathVariable(required = false) String platform, @RequestParam String code) {
        String accessToken = oauthAccountService.login(platform, code);
        return ResponseEntity.ok(Map.of("accessToken", accessToken, "message", "success"));
    }
    @Getter
    private static class Result<T> {
        private final T data;
        private final String message;

        public Result(T data, String message) {
            this.data = data;
            this.message = message;
        }
    }
}
