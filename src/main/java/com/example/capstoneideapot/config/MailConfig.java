package com.example.capstoneideapot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

// 1 => 회원가입
// 2 => 아이디 찾기
// 3 => 비밀번호 찾기
@Configuration
public class MailConfig {

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setHost("smtp.naver.com");
        javaMailSender.setUsername("ideapot_@naver.com");
        javaMailSender.setPassword("Brain-Compiler!");
        javaMailSender.setPort(465);
        javaMailSender.setJavaMailProperties(getMailProperties());

        return javaMailSender;
    }

    private Properties getMailProperties() {
        Properties properties = new Properties();

        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.debug", "true");
        properties.setProperty("mail.smtp.ssl.trust", "smtp.naver.com");
        properties.setProperty("mail.smtp.ssl.enable", "true");

        return properties;
    }

}