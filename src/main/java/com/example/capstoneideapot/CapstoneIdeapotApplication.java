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
    public BCryptPasswordEncoder passwordEncoder()   {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner run(UserRepository userRepository, RoleRepository roleRepository, FilesRepository filesRepository, PasswordEncoder passwordEncoder) {
        return args -> {
//            userRepository.save(new User(null, "nicenicnic123", passwordEncoder.encode("1234"), "박대형", "nicenicnic123@gmail.com", 0, 0, LocalDateTime.now(), null, null));
//            userRepository.save(new User(null, "nicenicnic1234", passwordEncoder.encode("1234"), "박대형", "eoguddl.dev@gmail.com", 0, 0, LocalDateTime.now(), null, null));
//
//            roleRepository.save(new Role(null, "ROLE_USER"));
//            roleRepository.save(new Role(null, "ROLE_ADMIN"));
//
//            filesRepository.save(new File(null, "profile", "basicProfile"));
        };
    };

}
