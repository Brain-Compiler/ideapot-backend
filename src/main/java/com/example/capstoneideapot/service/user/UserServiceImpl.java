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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

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
    public ResponseEntity<User> findUserById(Long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public ResponseEntity<?> findUsername(FindUserIdDto findUserIdDto) {
        try {
            String email = findUserIdDto.getEmail();
            String code = findUserIdDto.getCode();

            if (authTokenService.checkAuthCode(email, code)) {
                Map<String, String> id = new HashMap<>();
                String name = findUserIdDto.getName();
                String username = userRepository.findByNameAndEmail(name, email).getUsername();

                int idLength = username.length();
                username = username.substring(0, idLength / 2);

                for (int i = 0; i < idLength / 2; i++) {
                    username += "*";
                }

                if (idLength % 2 != 0) {
                    username += "*";
                }
                id.put("id", username);

                return new ResponseEntity<>(id, HttpStatus.OK);
            } else {
                ErrorDto error = new ErrorDto("인증코드 불일치 또는 만료");
                return new ResponseEntity<>(error, HttpStatus.CONFLICT);
            }
        } catch (Exception exception) {
            log.info("error: {}", exception.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Boolean> findPasswordCheckAuthCode(FindUserPasswordDto findUserPasswordDto) {
        String username = findUserPasswordDto.getUsername();
        String email = userRepository.findByUsername(username).getEmail();
        String code = findUserPasswordDto.getCode();

        if (!authTokenService.checkAuthCode(email, code)) {
            return new ResponseEntity<>(false, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Boolean> emailAuthCheckCode(EmailAuthenticationCheckDto emailAuthCheckDto) {
        String email = emailAuthCheckDto.getEmail();
        String code = emailAuthCheckDto.getCode();

        if (!authTokenService.checkAuthCode(email, code)) {
            return new ResponseEntity<>(false, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    // POST
    @Override
    public ResponseEntity<HttpStatus> findPasswordCertificationEmail(String username) throws Exception {
        try {
            User user = userRepository.findByUsername(username);

            if (user == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } else {
                String name = user.getName();
                String email = user.getEmail();

                authTokenService.sendAuthMail(new EmailAuthenticationDto(name, email), 2);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception exception) {
            log.info("error: {}", exception.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override  // 다시 짜 대형
    public User saveUser(SignUpDto signUpDto, MultipartFile profile) throws IOException {
        User user = createUser(signUpDto);

        return filesService.saveUserAndProfile(user, profile);
    }

    // PUT
    @Override
    public ResponseEntity<Boolean> changePassword(ChangePasswordDto changePasswordDto) {
        try {
            String password = changePasswordDto.getPassword();
            String passwordCheck = changePasswordDto.getPasswordCheck();

            if (password.equals(passwordCheck)) {
                String username = changePasswordDto.getUsername();
                User user = userRepository.findByUsername(username);

                user.setPassword(passwordEncoder.encode(password));

                userRepository.save(user);
            } else {
                return new ResponseEntity<>(false, HttpStatus.CONFLICT);
            }
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception exception) {
            log.info("error: {}", exception.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<Boolean> changePasswordByUser(ChangePasswordDto changePasswordDto) {
        if (checkPassword(changePasswordDto)) {
            changePassword(changePasswordDto);
        } else {
            return new ResponseEntity<>(false, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    // DELETE
    @Override
    public ResponseEntity<Boolean> userWithDraw(UserWithDrawDto userWithDrawDto) {
        String username = userWithDrawDto.getUsername();
        String password = userWithDrawDto.getPassword();
        User user = userRepository.findByUsername(username);
        String userPassword = user.getPassword();

        if (!passwordEncoder.matches(password, userPassword)) {
            return new ResponseEntity<>(false, HttpStatus.CONFLICT);
        } else {
            user.setStatus(2);
            userRepository.save(user);
        }
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    // ELSE
    @Override
    public ResponseEntity<Boolean> checkDuplicateUsername(String username) {
        if (getUserByUsername(username) != null) {
            return new ResponseEntity<>(false, HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> checkSignUp(SignUpDto signUpDto, MultipartFile profile) {
        try {
            ErrorDto error = new ErrorDto("없음");

            // 비밀번호 일치 여부 확인
            if (signUpDto.getPassword().equals(signUpDto.getPasswordCheck())) {
                // 이미 가입되어 있는 아이디인지 확인
                if (userRepository.findByUsername(signUpDto.getUsername()) == null) {
                    // 이미 가입되어 있는 이메일인지 확인
                    if (!(userRepository.findByEmail(signUpDto.getEmail()) == null)) {
                        error.setError("이미 가입되어 있는 이메일");
                        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
                    }
                } else {
                    error.setError("이미 가입되어 있는 아이디");
                    return new ResponseEntity<>(error, HttpStatus.CONFLICT);
                }
            } else {
                error.setError("비밀번호 불일치");
                return new ResponseEntity<>(error, HttpStatus.CONFLICT);
            }
            User user = saveUser(signUpDto, profile);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception exception) {
            log.info("exception: {}", exception.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public boolean checkPassword(ChangePasswordDto changePasswordDto) {
        String username = changePasswordDto.getUsername();
        String userPassword = userRepository.findByUsername(username).getPassword();
        String originalPassword = changePasswordDto.getOriginalPassword();

        return passwordEncoder.matches(originalPassword, userPassword);
    }

    @Override
    public User createUser(SignUpDto signUpDto) {
        User user = User.builder()
                .username(signUpDto.getUsername())
                .password(passwordEncoder.encode(signUpDto.getPassword()))
                .name(signUpDto.getName())
                .email(signUpDto.getEmail())
                .status(0)
                .createAt(LocalDateTime.now())
                .roles(new HashSet<>())
                .build();

        addBasicRoleToUser(user);
        return user;
    }

    @Override
    public void addBasicRoleToUser(User user) {
        Role role = roleRepository.findByName("ROLE_USER");
        user.getRoles().add(role);
    }
}
