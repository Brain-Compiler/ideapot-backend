package com.example.capstoneideapot.service.mail;

import com.example.capstoneideapot.entity.AuthToken;
import com.example.capstoneideapot.entity.dto.user.EmailAuthenticationDto;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public interface AuthTokenService {

    // GET

    // POST
    MimeMessage createAuthMessage(String name, String email, int type) throws Exception;

    // PUT

    // DELETE

    // ELSE
    Boolean checkAuthCode(String email, String code);

    void sendAuthMail(EmailAuthenticationDto emailAuthDto, int type) throws Exception;

    AuthToken createAuthToken(String email, String code);

}
