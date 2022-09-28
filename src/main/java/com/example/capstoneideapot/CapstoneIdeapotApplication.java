package com.example.capstoneideapot;

import com.example.capstoneideapot.entity.*;
import com.example.capstoneideapot.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@SpringBootApplication
public class CapstoneIdeapotApplication {

    public static void main(String[] args) {
        SpringApplication.run(CapstoneIdeapotApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner run(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            userRepository.save(new User(null, "nicenicnic123", passwordEncoder.encode("1234"), "nicenicnic123@gmail.com", "박대형", "baseProfile", 0, LocalDateTime.now(), null));

            roleRepository.save(new Role(null, "ROLE_USER"));
            roleRepository.save(new Role(null, "ROLE_ADMIN"));
//
//            ideaRepository.save(new Idea(null, 1L, "대형이", "설명", 1, 1000, LocalDateTime.now(), null, null));
//
//            fileRepository.save(new File(null, 1L, "아이디어", "무슨 사진", "경로", LocalDateTime.now(), null, null, null, null));
//
//            ideaFileRepository.save(new IdeaFile(null, ideaRepository.findById(1L).orElse(null), fileRepository.findById(1L).orElse(null)));
        };
    };

}
