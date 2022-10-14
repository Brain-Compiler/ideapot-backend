package com.example.capstoneideapot.entity.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailAuthenticationCheckDto {

    private String email;

    private String code;

}
