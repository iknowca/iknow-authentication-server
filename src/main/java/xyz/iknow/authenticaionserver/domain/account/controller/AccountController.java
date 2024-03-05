package xyz.iknow.authenticaionserver.domain.account.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import xyz.iknow.authenticaionserver.domain.account.dto.AccountDTO;
import xyz.iknow.authenticaionserver.domain.account.dto.LocalAccountDTO;
import xyz.iknow.authenticaionserver.domain.account.entity.Account;
import xyz.iknow.authenticaionserver.domain.account.service.AccountService;
import xyz.iknow.authenticaionserver.security.customUserDetails.CustomUserDetails;
import xyz.iknow.authenticaionserver.utility.response.DTOResponseBody;
import xyz.iknow.authenticaionserver.utility.response.MessageResponseBody;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
@Tag(name = "account", description = "계정 관련 API")
public class AccountController {
    final private AccountService accountService;

    @PostMapping(value = "/validate-email")
    @Operation(summary = "이메일 중복 검사", description = "이메일 중복 검사를 수행합니다.")
    @Parameter(hidden = true)
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(ref = "validateEmailRequest")))
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "이메일 중복 검사 성공", content = @Content(mediaType = "application/json", schema = @Schema(ref = "MessageResponse"))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "이메일 형식 오류", content = @Content(mediaType = "application/json", schema = @Schema(ref = "invalidEmailResponse"))),
    })
    public ResponseEntity<MessageResponseBody> validateEmail(@RequestBody LocalAccountDTO request) {
        Boolean result = accountService.validateEamil(request.getEmail());
        return result ? ResponseEntity.ok(new MessageResponseBody("NOT_REGISTERED_EMAIL", "success")) :
                ResponseEntity.ok(new MessageResponseBody("REGISTERED_EMAIL", "failure"));
    }

    @Operation(summary = "회원 가입", description = "회원 가입을 수행합니다.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(ref = "JoinAccountRequest")))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원 가입 성공", content = @Content(mediaType = "application/json", schema = @Schema(ref = "joinSuccessResponse"))),
            @ApiResponse(responseCode = "400", description = "이메일 형식 오류", content = @Content(mediaType = "application/json", schema = @Schema(ref = "invalidEmailResponse"))),
            @ApiResponse(responseCode = "409", description = "이메일 중복", content = @Content(mediaType = "application/json", schema = @Schema(ref = "emailDuplicatedResponse")))
    })
    @PostMapping("/join")
    public ResponseEntity<MessageResponseBody> join(@RequestBody LocalAccountDTO request) {
        accountService.createAccount(request);
        return ResponseEntity.created(URI.create("/account/login")).body(new MessageResponseBody("회원 가입 성공.", "success"));
    }

    @Operation(summary = "유저 정보 조회", description = "로그인 한 유저의 정보를 조회합니다.")
    @Parameter(name = "Authorization", description = "Bearer {token}", required = true, in = ParameterIn.HEADER, schema = @Schema(type = "string"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 정보 조회 성공", content = @Content(mediaType = "application/json", schema = @Schema(ref = "userInfoResponse"))),
            @ApiResponse(responseCode = "401", description = "로그인 필요", content = @Content(mediaType = "application/json", schema = @Schema(ref = "Unauthorized"))),
            @ApiResponse(responseCode = "404", description = "잘못된 접근", content = @Content(mediaType = "application/json", schema = @Schema(ref = "InvalidAccount")))
    })
    @GetMapping
    public ResponseEntity<DTOResponseBody<AccountDTO>> getMyInfo() {
        Account account = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAccount();
        AccountDTO accountDTO = accountService.getMyInfo(account);
        return ResponseEntity.ok(new DTOResponseBody<>(accountDTO, "success"));
    }

    @Operation(summary = "유저 정보 수정", description = "로그인 한 유저의 정보를 수정합니다.")
    @Parameter(name = "Authorization", description = "Bearer {token}", required = true, in = ParameterIn.HEADER, schema = @Schema(type = "string"))
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(schema = @Schema(ref = "UpdateAccountRequest"))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "유저 정보 수정 성공", content = @Content(schema = @Schema(ref = "userInfoResponse"))),
            @ApiResponse(responseCode = "401", description = "로그인 필요", content = @Content(mediaType = "application/json", schema = @Schema(ref = "Unauthorized"))),
            @ApiResponse(responseCode = "404", description = "잘못된 접근", content = @Content(mediaType = "application/json", schema = @Schema(ref = "InvalidAccount"))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content(mediaType = "application/json", schema = @Schema(ref = "InvalidUpdateRequest"))),
    })
    @PatchMapping
    public ResponseEntity<DTOResponseBody<AccountDTO>> updateMyInfo(@RequestBody AccountDTO request) {
        Account account = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAccount();
        AccountDTO accountDTO = accountService.updateMyInfo(account, request);
        return ResponseEntity.ok(new DTOResponseBody<>(accountDTO, "success"));
    }

    @Operation(summary = "로그아웃", description = "로그아웃을 수행합니다.")
    @Parameter(name = "Authorization", description = "Bearer {token}", required = true, in = ParameterIn.HEADER, schema = @Schema(type = "string"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공", content = @Content(mediaType = "application/json", schema = @Schema(ref = "MessageResponse"))),
            @ApiResponse(responseCode = "401", description = "로그인 필요", content = @Content(mediaType = "application/json", schema = @Schema(ref = "Unauthorized"))),
            @ApiResponse(responseCode = "404", description = "잘못된 접근", content = @Content(mediaType = "application/json", schema = @Schema(ref = "InvalidAccount"))),
    })
    @DeleteMapping("/logout")
    public ResponseEntity<MessageResponseBody> logout() {
        Account account = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAccount();
        accountService.logout(account);
        return ResponseEntity.ok(new MessageResponseBody("로그아웃 성공", "success"));
    }

    @Operation(summary = "회원 탈퇴", description = "회원 탈퇴를 수행합니다.")
    @Parameter(name = "Authorization", description = "Bearer {token}", required = true, in = ParameterIn.HEADER, schema = @Schema(type = "string"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 탈퇴 성공", content = @Content(mediaType = "application/json", schema = @Schema(ref = "MessageResponse"))),
            @ApiResponse(responseCode = "401", description = "로그인 필요", content = @Content(mediaType = "application/json", schema = @Schema(ref = "Unauthorized"))),
            @ApiResponse(responseCode = "404", description = "잘못된 접근", content = @Content(mediaType = "application/json", schema = @Schema(ref = "InvalidAccount"))),
    })
    @DeleteMapping
    public ResponseEntity<MessageResponseBody> withdrawAccount() {
        Account account = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAccount();
        accountService.withdrawAccount(account);
        return ResponseEntity.ok().body(new MessageResponseBody("회원 탈퇴 성공", "success"));
    }
    @Operation(summary = "비밀번호 변경", description = "비밀번호 변경을 수행합니다.")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(ref = "UpdateAccountPasswordRequest")))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "비밀번호 변경 성공", content = @Content(mediaType = "application/json", schema = @Schema(ref = "userInfoResponse"))),
            @ApiResponse(responseCode = "401", description = "로그인 필요", content = @Content(mediaType = "application/json", schema = @Schema(ref = "Unauthorized"))),
            @ApiResponse(responseCode = "404", description = "잘못된 접근", content = @Content(mediaType = "application/json", schema = @Schema(ref = "InvalidAccount"))),
    })
    @PatchMapping("/password")
    public ResponseEntity<MessageResponseBody> changePassword(@RequestBody LocalAccountDTO request) {
        Account account = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAccount();
        accountService.changePassword(account, request);
        return ResponseEntity.ok().body(new MessageResponseBody("passwordChangeSuccessful", "success"));
    }
}
