package com.example.capstoneideapot.service.mail;

import com.example.capstoneideapot.entity.AuthToken;
import com.example.capstoneideapot.entity.dto.user.EmailAuthenticationDto;
import com.example.capstoneideapot.repository.AuthTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthTokenServiceImpl implements AuthTokenService {

    private final JavaMailSender javaMailSender;

    private final AuthTokenRepository authTokenRepository;

    @Override
    public MimeMessage createAuthMessage(String name, String email, int type) throws Exception {
        MimeMessage message = javaMailSender.createMimeMessage();
        String code = UUID.randomUUID().toString().substring(0, 7);
        AuthToken authToken = createAuthToken(email, code);
        AuthToken token = authTokenRepository.findByEmail(email);

        int year = authToken.getExpirationDate().getYear();
        int month = authToken.getExpirationDate().getMonthValue();
        int day = authToken.getExpirationDate().getDayOfMonth();
        int hour = authToken.getExpirationDate().getHour();
        int min = authToken.getExpirationDate().getMinute();

        // 만약 토큰이 없다면 DB에 새로 저장, 있다면 토큰 덮어쓰기
        if (token == null) {
            authTokenRepository.save(authToken);
        } else {
            authToken.setId(token.getId());
            authTokenRepository.save(authToken);
        }

        message.setFrom("ideapot_@naver.com");
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
        message.setSubject("IdeaPot 인증코드");

        if (type == 0) {
            String title = "회원가입";
            mailForm(name, message, code, year, month, day, hour, min, title);
        } else if (type == 1) {
            String title = "아이디 찾기";
            mailForm(name, message, code, year, month, day, hour, min, title);
        } else if (type == 2) {
            String title = "비밀번호 찾기";
            mailForm(name, message, code, year, month, day, hour, min, title);
        }

        return message;
    }

    @Override
    public Boolean checkAuthCode(String email, String code) {
        try {
            AuthToken authToken = authTokenRepository.findByEmailAndExpirationDateAfterAndExpired(email, LocalDateTime.now(), false);
            if (authToken.getCode().equals(code)) {
                authToken.setTokenToUsed();
                authTokenRepository.save(authToken);
                return true;
            } else {
                return false;
            }
        } catch (Exception exception) {
            return false;
        }
    }

    @Override
    public void sendAuthMail(EmailAuthenticationDto emailAuthDto, int type) throws Exception {
        try {
            String name = emailAuthDto.getName();
            String email = emailAuthDto.getEmail();
            MimeMessage mailMessage = createAuthMessage(name, email, type);
            javaMailSender.send(mailMessage);
        } catch (MailException mailException) {
            mailException.printStackTrace();
            throw new IllegalAccessException();
        }
    }

    @Override
    public AuthToken createAuthToken(String email, String code) {
        AuthToken authToken = new AuthToken();
        authToken.setEmail(email);
        authToken.setCode(code);
        authToken.setExpirationDate(LocalDateTime.now().plusMinutes(3L));  // 3분 후 만료
        authToken.setExpired(false);
        return authToken;
    }

    private void mailForm(String name, MimeMessage message, String code, int year, int month, int day, int hour, int min, String title) throws MessagingException {
        message.setContent("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "<head>\n" +
                "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
                "  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\" />\n" +
                "  <title>HTML Email Template</title>\n" +
                "</head>\n" +

                "<body style=\" margin: 0; padding: 0; position: absolute; width: 100% !important; height: 100% !important; margin: 0; padding: 0; color: rgb(0, 0, 0);\">\n" +
                "<table id=\"bodyTable\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\" style=\" width: 100% !important; height: 100% !important; margin: 0; padding: 0; padding: 20px 0 30px 0; background-color: #ffffff;\">\n" +
                "<tr>\n" +
                "\t<td>\n" +
                "\t\t<table id=\"subTable\" cellpaddinng=\"0\" cellspacing=\"0\" border=\"0\" width=\"100%\">\n" +
                "\t\t<tr>\n" +
                "\t\t\t<td>\n" +
                "\t\t\t\t<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                "\t\t\t\t<tr>\n" +
                "\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t</td>\t\n" +
                "\t\t\t\t</tr>\n" +
                "\t\t\t\t</table>\n" +
                "\t\t\t</td>\n" +
                "\t\t</tr>\n" +
                "\t\t<tr>\n" +
                "\t\t\t<td>\n" +
                "\t\t\t\t<table id=\"mainTable\" cellpadding=\"0\" cellspacing=\"0\" width=\"600px\" style=\" margin: 0 auto; height: 720px; border: 1px solid rgba(70, 70, 70, 0.16); border-radius: 15px;vertical-align: middle; \">\n" +
                "\t\t\t\t<tr style=\"display: block; padding: 0 calc((600px - 452px) / 2);\">\n" +
                "\t\t\t\t\t<td class=\"title\" style=\"padding: 45px 0px 25px; font-size: 30px;\">\n" +
                "\t\t\t\t\t\t안녕하세요, <b>" + name + " </b>님\n" +
                "\t\t\t\t\t</td>\n" +
                "\t\t\t\t</tr>\n" +
                "\t\t\t\t<tr style=\"display: block; padding: 0 calc((600px - 452px) / 2);\">\n" +
                "\t\t\t\t\t<td class=\"subTitle\" style=\"font-size: 20px;\">\n" +
                "\t\t\t\t\t\t<b>IdeaPot</b> " + title + " 인증코드입니다.\n" +
                "\t\t\t\t\t</td>\n" +
                "\t\t\t\t</tr style>\n" +
                "\t\t\t\t<tr class=\"codeTr\" style=\"display: block; padding: 0 calc((600px - 452px) / 2); padding-top: 40px !important; padding-bottom: 20px !important;\">\n" +
                "\t\t\t\t\t<td class=\"code\" style=\"width: 450px; height: 90px; font-size: 40px; font-weight: 900; border: 1px solid rgba(0, 0, 0, 0.05); text-align: center; vertical-align: middle; border-left: #9776ff 5px solid;\">\n" +
                "\t\t\t\t\t\t " + code + "\n" +
                "\t\t\t\t\t</td>\n" +
                "\t\t\t\t</tr>\n" +
                "\t\t\t\t<tr class=\"exprationTr\" style=\"display: block; padding: 0 calc((600px - 452px) / 2); text-align: center; padding: 0 calc((600px - 432px) / 2) !important; padding-bottom: 40px !important;\">\n" +
                "\t\t\t\t\t<td class=\"expration\" style=\"width: 600px; color: rgba(0, 0, 0, 0.7); text-align: center; font-size: 12px; font-weight: 700;\">\n" +
                "\t\t\t\t\t\t 해당 코드는 KST " + year + " " + month + "월 " + day + "일 " + hour + "시 " + min + "분에 만료됩니다.\n" +
                "\t\t\t\t\t</td>\n" +
                "\t\t\t\t</tr>\n" +
                "\t\t\t\t<tr class=\"li\" style=\"display: block; padding: 0 calc((600px - 452px) / 2);\">\n" +
                "\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t<b>회원가입</b>\n" +
                "\t\t\t\t\t\t<ul style=\"padding-left: 0px; margin-top: 0;\">\n" +
                "\t\t\t\t\t\t\t<li style=\"padding-top: 5px !important; padding-bottom: 12px !important; padding-top: 5px; padding-bottom: 0px; font-size: 13px; list-style-type : none\">\n" +
                "\t\t\t\t\t\t\t· 회원가입을 시도한 적이 없다면 메일을 즉시 삭제해주세요.</li>\n" +
                "\t\t\t\t\t\t</ul>\n" +
                "\t\t\t\t\t</td>\n" +
                "\t\t\t\t</tr>\n" +
                "\t\t\t\t<tr class=\"li\" style=\"display: block; padding: 0 calc((600px - 452px) / 2);\">\n" +
                "\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t<b>아이디 찾기</b>\n" +
                "\t\t\t\t\t\t<ul style=\"padding-left: 0px; margin-top: 0;\">\n" +
                "\t\t\t\t\t\t\t<li style=\"padding-top: 5px !important; padding-bottom: 12px !important; padding-top: 5px; padding-bottom: 0px; font-size: 13px; list-style-type : none\">\n" +
                "\t\t\t\t\t\t\t· 아이디 찾기를 시도한 적이 없다면 비밀번호를 변경해주세요. </li>\n" +
                "\t\t\t\t\t\t</ul>\n" +
                "\t\t\t\t\t</td>\n" +
                "\t\t\t\t</tr>\n" +
                "\t\t\t\t<tr class=\"li\" style=\"display: block; padding: 0 calc((600px - 452px) / 2);\">\n" +
                "\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t<b>비밀번호 찾기</b>\n" +
                "\t\t\t\t\t\t<ul style=\"padding-left: 0px; margin-top: 0;\">\n" +
                "\t\t\t\t\t\t\t<li style=\"padding-top: 5px !important; padding-bottom: 12px !important; padding-top: 5px; padding-bottom: 0px; font-size: 13px; list-style-type : none\">\n" +
                "\t\t\t\t\t\t\t· 비밀번호 찾기를 시도한 적이 없다면 비밀번호를 변경해주세요. </li>\n" +
                "\t\t\t\t\t\t</ul>\n" +
                "\t\t\t\t\t</td>\n" +
                "\t\t\t\t</tr>\n" +
                "\t\t\t\t<tr class=\"footer\" style=\"display: block; padding: 0 calc((600px - 452px) / 2); padding-top: 30px !important;\">\n" +
                "\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t\t<b>인증코드 오작동</b> 시 아래 지침을 수행하고 재요청해보세요\n" +
                "\t\t\t\t\t\t<ul style=\"padding-left: 0px; margin-top: 0;\">\n" +
                "\t\t\t\t\t\t\t<li style=\"padding-top: 7px !important; padding-bottom: 5px !important; padding-top: 5px; padding-bottom: 0px; font-size: 13px; list-style-type : none\">· 다른 브라우저나 시크릿 탭을 사용하세요.</li>\n" +
                "\t\t\t\t\t\t\t<li style=\"padding-top: 0px !important; padding-bottom: 12px !important; padding-top: 5px; padding-bottom: 0px; font-size: 13px; list-style-type : none\">· 브라우저의 쿠키와 캐시를 삭제하세요.</li>\n" +
                "\t\t\t\t\t\t</ul>\n" +
                "\t\t\t\t\t</td>\n" +
                "\t\t\t\t</tr>\n" +
                "\t\t\t\t</table>\n" +
                "\t\t\t</td>\n" +
                "\t\t</tr>\n" +
                "\t\t<tr>\n" +
                "\t\t\t<td>\n" +
                "\t\t\t\t<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n" +
                "\t\t\t\t<tr>\n" +
                "\t\t\t\t\t<td>\n" +
                "\t\t\t\t\t</td>\n" +
                "\t\t\t\t</tr>\n" +
                "\t\t\t\t</table>\n" +
                "\t\t\t</td>\n" +
                "\t\t</tr>\n" +
                "\t\t</table>\n" +
                "\t</td>\n" +
                "</tr>\n" +
                "</table>\n" +
                "</body>\n" +
                "</html>", "text/html; charset=UTF-8");
    }

}