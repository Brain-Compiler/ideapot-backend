package com.example.capstoneideapot.entity.dto.idea;

import lombok.Data;

@Data
public class IdeaDto {

    private Long id;

    private Long userId;

    private String title;

    private String description;

    private String category1;

    private String category2;

    private String category3;

    private int price;

}
