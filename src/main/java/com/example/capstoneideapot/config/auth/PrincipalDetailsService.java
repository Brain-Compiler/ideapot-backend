package com.example.capstoneideapot.config.auth;

import com.example.capstoneideapot.entity.User;
import com.example.capstoneideapot.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override // 5: AuthenticationProvider에서 아이디를 조회 후 그 아이디를 기반으로 데이터를 조회
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("5");
        log.info("6");
        User user = userService.getUserByUsername(username); // 6: 아이디를 기반으로 데이터를 조회
        if (user == null) {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        } else {
            log.info("User found in the database: {}", username);
        }
        log.info("7");
        return new PrincipalDetails(user); // 7: 아이디를 기반으로 조회한 데이터를 반환
    }
}
