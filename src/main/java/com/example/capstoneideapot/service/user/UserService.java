package com.example.capstoneideapot.service.user;

import com.example.capstoneideapot.entity.User;
import com.example.capstoneideapot.entity.dto.ErrorDto;
import com.example.capstoneideapot.entity.dto.user.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public interface UserService {

    // GET
    ResponseEntity<User> findUserById(Long id);

    User getUserByUsername(String username);

    ResponseEntity<?> findUsername(FindUserIdDto findUserIdDto) throws Exception;

    ResponseEntity<Boolean> findPasswordCheckAuthCode(FindUserPasswordDto findUserPasswordDto);

    ResponseEntity<Boolean> emailAuthCheckCode(EmailAuthenticationCheckDto emailAuthCehckDto);

    // POST
    ResponseEntity<HttpStatus> findPasswordCertificationEmail(String username) throws Exception;

    User saveUser(SignUpDto signUpDto, MultipartFile profile) throws IOException;

    // PUT
    ResponseEntity<Boolean> changePassword(ChangePasswordDto changePasswordDto);

    ResponseEntity<Boolean> changePasswordByUser(ChangePasswordDto changePasswordDto);

    // DELETE
    ResponseEntity<Boolean> userWithDraw(UserWithDrawDto userWithDrawDto);

    // ELSE
    ResponseEntity<Boolean> checkDuplicateUsername(String username);

    ResponseEntity<?> checkSignUp(SignUpDto signUpDto, MultipartFile profile) throws IOException;

    boolean checkPassword(ChangePasswordDto changePasswordDto);

    User createUser(SignUpDto signUpDto);

    void addBasicRoleToUser(User user);

}
