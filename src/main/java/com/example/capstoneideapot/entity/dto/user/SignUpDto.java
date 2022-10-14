package com.example.capstoneideapot.entity.dto.user;

import lombok.Data;

@Data
public class SignUpDto {

    private String name;

    private String username;

    private String password;

    private String passwordCheck;

    private String email;

}
