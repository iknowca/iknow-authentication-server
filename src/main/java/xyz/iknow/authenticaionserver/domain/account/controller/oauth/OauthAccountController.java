package xyz.iknow.authenticaionserver.domain.account.controller.oauth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import xyz.iknow.authenticaionserver.domain.account.service.oauth.OauthAccountService;
import xyz.iknow.authenticaionserver.utility.response.DTOResponseBody;
import xyz.iknow.authenticaionserver.utility.response.MessageResponseBody;

@Tag(name = "oauth account", description = "oauth 계정 전용 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/account/oauth")
public class OauthAccountController {
    private final OauthAccountService oauthAccountService;

    @Operation(summary = "oauth 로그인 URL", description = "oauth 로그인 URL을 반환합니다.")
    @Parameter(name = "platform", description = "oauth 플랫폼", required = true, in = ParameterIn.PATH, schema = @io.swagger.v3.oas.annotations.media.Schema())
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "oauth 로그인 URL 반환 성공", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(ref = "oauthLoginUrlResponse"))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "oauth 플랫폼 없음", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(type = "string", example = "InvalidPlatform")))
    })
    @GetMapping("/url/{platform}")
    public ResponseEntity<MessageResponseBody> getOauthUrl(@PathVariable(required = false) String platform) {
        String loginUrl = oauthAccountService.getOauthUrl(platform);
        return ResponseEntity.ok(new MessageResponseBody(loginUrl, "success"));
    }

    @Operation(summary = "oauth 로그인", description = "oauth 로그인을 수행합니다.")
    @Parameter(name = "platform", description = "oauth 플랫폼", required = true, in = ParameterIn.PATH, schema = @io.swagger.v3.oas.annotations.media.Schema(type = "string", example = "KAKAO"))
    @Parameter(name = "code", description = "oauth 인증 코드", required = true, in = ParameterIn.QUERY, schema = @io.swagger.v3.oas.annotations.media.Schema(type = "string", example = "code"))
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "oauth 로그인 성공", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(ref = "oauthLoginResponse"))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "oauth 플랫폼 없음", content = @io.swagger.v3.oas.annotations.media.Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(type = "string", example = "InvalidPlatform")))
    })
    @GetMapping("/callback/{platform}")
    public ResponseEntity<MessageResponseBody> login(@PathVariable(required = false) String platform, @RequestParam String code) {
        String accessToken = oauthAccountService.login(platform, code);
        return ResponseEntity.ok(new MessageResponseBody(accessToken, "success"));
    }
}
