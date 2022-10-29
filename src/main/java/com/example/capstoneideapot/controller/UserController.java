package com.example.capstoneideapot.controller;

import com.example.capstoneideapot.entity.User;
import com.example.capstoneideapot.entity.dto.ErrorDto;
import com.example.capstoneideapot.entity.dto.user.*;
import com.example.capstoneideapot.service.mail.AuthTokenService;
import com.example.capstoneideapot.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    private final AuthTokenService authTokenService;

    // GET
    @GetMapping("/find-user/{id}")  // Test // User 찾기
    public User findUserById(@PathVariable Long id) {
        return  userService.findUserById(id);
    }

    @PostMapping("/find-id")  // 아이디 찾기 // GET
    public ResponseEntity<?> findId(@RequestBody FindUserIdDto findUserIdDto) throws Exception {
        return userService.findUsername(findUserIdDto);
    }

    @PostMapping("/find-password/check-authcode")  // 비밀번호 찾기 -> 이메일 인증 코드 확인 // GET
    public ErrorDto findPasswordCheckAuthCode(@RequestBody FindUserPasswordDto findUserPasswordDto) {
        return userService.findPasswordCheckAuthCode(findUserPasswordDto);
    }

    @PostMapping("/email-authentication/check-code") // GET
    public ErrorDto emailAuthCheckCode(@RequestBody EmailAuthenticationCheckDto emailAuthCheckDto) {
        return userService.emailAuthCheckCode(emailAuthCheckDto);
    }

    // POST
    @PostMapping("/email-authentication/sign-up")  // 이메일 인증 - 회원가입
    public void signUpCertificationEmail(@RequestBody EmailAuthenticationDto emailAuthDto) throws Exception {
        authTokenService.sendAuthMail(emailAuthDto, 0);
    }

    @PostMapping("/email-authentication/find-id")  // 이메일 인증 - 아이디 찾기
    public void findIdCertificationEmail(@RequestBody EmailAuthenticationDto emailAuthDto) throws Exception {
        authTokenService.sendAuthMail(emailAuthDto, 1);
    }

    @PostMapping("/email-authentication/find-password")  // 비밀번호 찾기 -> 사용자가 있는 지 확인 -> 있다면 해당하는 유저에게 이메일 전송
    public ErrorDto findPasswordCertificationEmail(@RequestParam String username) throws Exception {
        return userService.findPasswordCertificationEmail(username);
    }

    @PostMapping("/sign-up")  // 회원가입
    public ResponseEntity<?> signUp(@RequestBody SignUpDto signUpDto, @RequestPart(value = "static/profile", required = false) MultipartFile file) throws IOException {
        ErrorDto error = userService.checkSignUp(signUpDto);
        if (error.getError().equals("없음")) {
            userService.saveUser(signUpDto, file);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body(error);
        }
    }

    // PUT
    @PutMapping("/find-password")  // 비밀번호 찾기 -> 비밀번호 변경(찾기)
    public ErrorDto changePasswordByFind(@RequestBody ChangePasswordDto changePasswordDto) {
        return userService.changePassword(changePasswordDto);
    }

    @PutMapping("/change-password")  // 비밀번호 변경(유저)
    public ErrorDto changePasswordByUser(@RequestBody ChangePasswordDto changePasswordDto) {
        return userService.changePasswordByUser(changePasswordDto);
    }

    @PutMapping("/user-withdraw")  // 회원탈퇴
    public ErrorDto changePassword(@RequestBody UserWithDrawDto userWithDrawDto) {
        return userService.userWithDraw(userWithDrawDto);
    }

    // DELETE

    // ELSE Test
    @GetMapping("/id-duplicate-check")  // 아이디 중복 체크
    public ErrorDto checkDuplicateUsername(@RequestParam String username) {
        return userService.checkDuplicateUsername(username);
    }

}
