package com.example.capstoneideapot.service.user;

import com.example.capstoneideapot.entity.User;
import com.example.capstoneideapot.entity.dto.ErrorDto;
import com.example.capstoneideapot.entity.dto.user.*;
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

    ErrorDto findPasswordCheckAuthCode(FindUserPasswordDto findUserPasswordDto);

    ResponseEntity<Boolean> emailAuthCheckCode(EmailAuthenticationCheckDto emailAuthCehckDto);

    // POST
    ErrorDto findPasswordCertificationEmail(String username) throws Exception;

    User saveUser(SignUpDto signUpDto, MultipartFile profile) throws IOException;

    // PUT
    ErrorDto changePassword(ChangePasswordDto changePasswordDto);

    ErrorDto changePasswordByUser(ChangePasswordDto changePasswordDto);

    // DELETE
    ErrorDto userWithDraw(UserWithDrawDto userWithDrawDto);

    // ELSE
    ResponseEntity<Boolean> checkDuplicateUsername(String username);

    ResponseEntity<?> checkSignUp(SignUpDto signUpDto, MultipartFile profile) throws IOException;

    ErrorDto checkPassword(ChangePasswordDto changePasswordDto);

    User createUser(SignUpDto signUpDto);

    void addBasicRoleToUser(User user);

}
