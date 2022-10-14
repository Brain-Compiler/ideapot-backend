package com.example.capstoneideapot.service.user;

import com.example.capstoneideapot.entity.AuthToken;
import com.example.capstoneideapot.entity.Role;
import com.example.capstoneideapot.entity.User;
import com.example.capstoneideapot.entity.dto.ErrorDto;
import com.example.capstoneideapot.entity.dto.user.*;
import com.example.capstoneideapot.repository.*;
import com.example.capstoneideapot.service.files.FilesService;
import com.example.capstoneideapot.service.mail.AuthTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final FilesService filesService;

    private final AuthTokenService authTokenService;

    private final PasswordEncoder passwordEncoder;

    // GET

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public ResponseEntity<?> findUsername(FindUserIdDto findUserIdDto) {
        Map<String, String> id = new HashMap<>();
        String email = findUserIdDto.getEmail();
        String code = findUserIdDto.getCode();

        if (authTokenService.checkAuthCode(email, code)) {
            String name = findUserIdDto.getName();
            String username = userRepository.findByNameAndEmail(name, email).getUsername();

            id.put("id", username);

            return ResponseEntity.ok(id);
        } else {
            return ResponseEntity.ok(new ErrorDto("인증코드 불일치 또는 만료"));
        }
    }

    @Override
    public ErrorDto findPassword(String username) throws Exception {
        ErrorDto error = new ErrorDto("없음");

        if (userRepository.findByUsername(username) == null) {
            error.setError("일치하는 아이디가 없음");
        } else {
            User user = userRepository.findByUsername(username);
            String email = user.getEmail();
            String name = user.getName();

            authTokenService.sendAuthMail(new EmailAuthenticationDto(name, email), 2);
        }

        return error;
    }

    @Override
    public ErrorDto findPasswordCheckAuthCode(FindUserPasswordDto findUserPasswordDto) {
        String username = findUserPasswordDto.getUsername();
        String email = userRepository.findByUsername(username).getEmail();
        String code = findUserPasswordDto.getCode();
        ErrorDto error = new ErrorDto("없음");

        if (!authTokenService.checkAuthCode(email, code)) {
            error.setError("인증코드 불일치");
        }
        return error;
    }

    @Override
    public ErrorDto emailAuthCheckCode(EmailAuthenticationCheckDto emailAuthCheckDto) {
        ErrorDto error = new ErrorDto("없음");
        String email = emailAuthCheckDto.getEmail();
        String code = emailAuthCheckDto.getCode();

        if (!authTokenService.checkAuthCode(email, code)) {
            error.setError("인증코드 불일치 또는 만료");
        }
        return error;
    }

    // POST
    @Override  // 다시 짜 대형
    public void saveUser(SignUpDto signUpDto, MultipartFile profile) throws IOException {
        String path = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\profile";
        String profileName;

        if (profile != null) {
            profileName = filesService.saveFileAndReturnFileName(path, profile);
        } else {
            profileName = "baseProfile";
        }
        userRepository.save(createUser(signUpDto, profileName));
    }

    // PUT
    @Override
    public ErrorDto changePassword(ChangePasswordDto changePasswordDto) {
        ErrorDto error = new ErrorDto("없음");
        String password = changePasswordDto.getPassword();
        String passwordCheck = changePasswordDto.getPasswordCheck();
        if (password.equals(passwordCheck)) {
            String username = changePasswordDto.getUsername();
            User user = userRepository.findByUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
        } else {
            error.setError("비밀번호 불일치");
        }
        return error;
    }

    @Override
    public ErrorDto changePasswordByUser(ChangePasswordDto changePasswordDto) {
        ErrorDto error = checkPassword(changePasswordDto);
        if (error.getError().equals("없음")) {
            error = changePassword(changePasswordDto);
        }
        return error;
    }

    // DELETE
    @Override
    public ErrorDto userWithDraw(UserWithDrawDto userWithDrawDto) {
        ErrorDto error = new ErrorDto("없음");
        String username = userWithDrawDto.getUsername();
        String password = userWithDrawDto.getPassword();
        User user = userRepository.findByUsername(username);
        String userPassword = user.getPassword();

        if (!passwordEncoder.matches(password, userPassword)) {
            error.setError("기존 비밀번호 불일치");
        } else {
            user.setStatus(2);
            userRepository.save(user);
        }
        return error;
    }

    // ELSE
    @Override
    public ErrorDto checkDuplicateUsername(String username) {
        ErrorDto error = new ErrorDto("없음");
        if (getUserByUsername(username) != null) {
            error.setError("아이디 중복");
        }
        return error;
    }

    @Override
    public ErrorDto checkSignUp(SignUpDto signUpDto) {
        ErrorDto error = new ErrorDto("없음");

        // 비밀번호 일치 여부 확인
        if (signUpDto.getPassword().equals(signUpDto.getPasswordCheck())) {
            // 이미 가입되어 있는 아이디인지 확인
            if (userRepository.findByUsername(signUpDto.getUsername()) == null) {
                // 이미 가입되어 있는 이메일인지 확인
                if (!(userRepository.findByEmail(signUpDto.getEmail()) == null)) {
                    error.setError("이미 가입되어 있는 이메일");
                }
            } else {
                error.setError("이미 가입되어 있는 아이디");
            }
        } else {
            error.setError("비밀번호 불일치");
        }
        return error;
    }

    @Override
    public ErrorDto checkPassword(ChangePasswordDto changePasswordDto) {
        ErrorDto error = new ErrorDto("없음");
        String username = changePasswordDto.getUsername();
        String userPassword = userRepository.findByUsername(username).getPassword();
        String originalPassword = changePasswordDto.getOriginalPassword();

        if (!passwordEncoder.matches(originalPassword, userPassword)) {
            error.setError("기존 비밀번호 불일치");
        }
        return error;
    }

    @Override
    public User createUser(SignUpDto signUpDto, String profileName) {
        User user = new User();
        user.setUsername(signUpDto.getUsername());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        user.setName(signUpDto.getName());
        user.setEmail(signUpDto.getEmail());
        user.setProfile(profileName);
        user.setStatus(0);
        user.setCreateAt(LocalDateTime.now());
        addBasicRoleToUser(user);
        return user;
    }

    @Override
    public void addBasicRoleToUser(User user) {
        Role role = roleRepository.findByName("ROLE_USER");
        user.getRoles().add(role);
    }
}
