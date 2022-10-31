package com.example.capstoneideapot.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String discription;

    // 문의 답변
    private String answer;

    @OneToOne
    private User user;

    @OneToMany
    private Set<File> files = new HashSet<>();

}
