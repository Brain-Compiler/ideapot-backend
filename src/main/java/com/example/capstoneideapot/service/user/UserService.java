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
    User findUserById(Long id);

    User getUserByUsername(String username);

    ResponseEntity<?> findUsername(FindUserIdDto findUserIdDto) throws Exception;

    ErrorDto findPassword(String username) throws Exception;

    ErrorDto findPasswordCheckAuthCode(FindUserPasswordDto findUserPasswordDto);

    // POST
    void saveUser(SignUpDto signUpDto, MultipartFile profile) throws IOException;

    // PUT
    ErrorDto changePassword(ChangePasswordDto changePasswordDto);

    ErrorDto changePasswordByUser(ChangePasswordDto changePasswordDto);

    // DELETE
    ErrorDto userWithDraw(UserWithDrawDto userWithDrawDto);

    // ELSE
    ErrorDto checkDuplicateUsername(String username);

    ErrorDto checkSignUp(SignUpDto signUpDto);

    ErrorDto checkPassword(ChangePasswordDto changePasswordDto);

    User createUser(SignUpDto signUpDto, String profileName);

    void addBasicRoleToUser(User user);

}
