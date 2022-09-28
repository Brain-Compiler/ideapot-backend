package com.example.capstoneideapot.repository;

import com.example.capstoneideapot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public User findByUsername(String username);

    public User findByEmail(String email);

    public User findByNameAndEmail(String name, String email);

}
