package com.example.capstoneideapot.config.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.capstoneideapot.config.auth.PrincipalDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    @Override  // 인증 시도 // 2: UsernamePasswordAuthenticationToken 발급
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("1");
        log.info("2");
        String username = request.getParameter("username"); // 2-1: Username 파싱
        String password = request.getParameter("password"); // 2-2: Password 파싱
        log.info("User info : {}, {}", username, password);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        log.info("3");
        log.info("4");
        return authenticationManager.authenticate(authenticationToken);
        // 3: AuthenticationManager에게 UsernamePasswordAuthenticationToken 전달
        // 4: AuthenticationManager는 여러 인증 처리 AuthenticationProvider가 존재 -> 전달
    }

    @Override  // 인증 성공 // 로그인 성공하면 호출 // 8: 인증 성공시 토큰 발급
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException {
        log.info("8");
        PrincipalDetails user = (PrincipalDetails) authentication.getPrincipal();
        int userStatus = user.getUser().getStatus();
        Map<String, String> res = new HashMap<>();

        if (userStatus == 0) {
            Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());

            String access_token = JWT.create()
                    .withSubject(user.getUsername())
                    .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                    .withIssuer(request.getRequestURL().toString())
                    .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                    .sign(algorithm);

            res.put("access_token", access_token);
        } else if (userStatus == 1) {
            res.put("user_status", "비활성화");
        } else if (userStatus == 2) {
            res.put("user_status", "탈퇴");
        }
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), res);
    }
}