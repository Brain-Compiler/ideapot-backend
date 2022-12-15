package com.example.capstoneideapot.entity.dto.idea;

import com.example.capstoneideapot.entity.Category;
import com.example.capstoneideapot.entity.File;
import com.example.capstoneideapot.entity.User;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class IdeaLDto {

    private Long id;

    private User user;

    private Category category;

    private String title;

    private String description;

    private String price;

    private int status;

    private String createdAt;

    private String editAt;

    private Set<File> files = new HashSet<>();

}
