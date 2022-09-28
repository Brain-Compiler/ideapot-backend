package com.example.capstoneideapot.entity.dto;

import lombok.Data;

@Data
public class ErrorDto {

    private String error;

    public ErrorDto(String error) {
        this.error = error;
    }
}
