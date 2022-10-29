package com.example.capstoneideapot.repository;

import com.example.capstoneideapot.entity.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface AuthTokenRepository extends JpaRepository<AuthToken, Long> {

    public AuthToken findByEmail(String email);

    public AuthToken findByEmailAndExpirationDateAfterAndExpired(String email, LocalDateTime now, boolean expired);

}
