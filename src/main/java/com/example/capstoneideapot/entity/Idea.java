package com.example.capstoneideapot.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Idea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @NotNull
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    // @NotNull
    @OneToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @NotNull
    private String title;

    @NotNull
    private String description;

    @NotNull
    private int price;

    @NotNull
    private int status;

    @NotNull
    private LocalDateTime createdAt;

    private LocalDateTime editAt;

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Files> files = new HashSet<>();

}
