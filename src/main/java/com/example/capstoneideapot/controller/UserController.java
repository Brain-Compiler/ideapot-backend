package com.example.capstoneideapot.controller;

import com.example.capstoneideapot.entity.User;
import com.example.capstoneideapot.entity.dto.user.*;
import com.example.capstoneideapot.service.mail.AuthTokenService;
import com.example.capstoneideapot.service.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "계정", description = "계정 관련 api입니다.")
public class UserController {

    private final UserService userService;

    private final AuthTokenService authTokenService;

    // GET
    @GetMapping("/find-user/{id}")  // Test // User 찾기
    public ResponseEntity<User> findUserById(@PathVariable Long id) {
        return userService.findUserById(id);
    }

    @PostMapping("/find-id")  // 아이디 찾기 // GET
    public ResponseEntity<?> findId(@RequestBody FindUserIdDto findUserIdDto) throws Exception {
        return userService.findUsername(findUserIdDto);
    }

    @PostMapping("/find-password/check-authcode")  // 비밀번호 찾기 -> 이메일 인증 코드 확인 // GET
    public ResponseEntity<Boolean> findPasswordCheckAuthCode(@RequestBody FindUserPasswordDto findUserPasswordDto) {
        return userService.findPasswordCheckAuthCode(findUserPasswordDto);
    }

    @PostMapping("/email-authentication/check-code") // GET
    public ResponseEntity<Boolean> emailAuthCheckCode(@RequestBody EmailAuthenticationCheckDto emailAuthCheckDto) {
        return userService.emailAuthCheckCode(emailAuthCheckDto);
    }

    // POST
    @PostMapping("/email-authentication/sign-up")  // 이메일 인증 - 회원가입
    public ResponseEntity<HttpStatus> signUpCertificationEmail(@RequestBody EmailAuthenticationDto emailAuthDto) throws Exception {
        return authTokenService.sendAuthMail(emailAuthDto, 0);
    }

    @PostMapping("/email-authentication/find-id")  // 이메일 인증 - 아이디 찾기
    public ResponseEntity<HttpStatus> findIdCertificationEmail(@RequestBody EmailAuthenticationDto emailAuthDto) throws Exception {
        return authTokenService.sendAuthMail(emailAuthDto, 1);
    }

    @PostMapping("/email-authentication/find-password")  // 비밀번호 찾기 -> 사용자가 있는 지 확인 -> 있다면 해당하는 유저에게 이메일 전송
    public ResponseEntity<HttpStatus> findPasswordCertificationEmail(@RequestParam String username) throws Exception {
        return userService.findPasswordCertificationEmail(username);
    }

    @Operation(summary = "회원가입 메소드", description = "회원가입 메서드입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = User.class))),
            @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
            @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
    })
    @Parameters({

    })
    @PostMapping("/sign-up")  // 회원가입
    public ResponseEntity<?> signUp(@RequestPart SignUpDto signUpDto,
                                    @RequestPart(required = false) MultipartFile profile) throws IOException {
        return userService.checkSignUp(signUpDto, profile);
    }

    // PUT
    @PutMapping("/find-password")  // 비밀번호 찾기 -> 비밀번호 변경(찾기)
    public ResponseEntity<Boolean> changePasswordByFind(@RequestBody ChangePasswordDto changePasswordDto) {
        return userService.changePassword(changePasswordDto);
    }

    @PutMapping("/change-password")  // 비밀번호 변경(유저)
    public ResponseEntity<Boolean> changePasswordByUser(@RequestBody ChangePasswordDto changePasswordDto) {
        return userService.changePasswordByUser(changePasswordDto);
    }

    @PutMapping("/user-withdraw")  // 회원탈퇴
    public ResponseEntity<Boolean> changePassword(@RequestBody UserWithDrawDto userWithDrawDto) {
        return userService.userWithDraw(userWithDrawDto);
    }

    // DELETE

    // ELSE
    @GetMapping("/id-duplicate-check")  // 아이디 중복 체크
    public ResponseEntity<Boolean> checkDuplicateUsername(@RequestParam String username) {
        return userService.checkDuplicateUsername(username);
    }

}
