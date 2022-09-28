package com.example.capstoneideapot.entity.dto.user;

import lombok.Data;

@Data
public class ChangePasswordDto {

    private String username;

    private String originalPassword;

    private String password;

    private String passwordCheck;

}
