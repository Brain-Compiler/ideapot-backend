package com.example.capstoneideapot.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @JsonIgnore
    private String username;

    @NotNull
    @JsonIgnore
    private String password;

    @NotNull
    private String name;

    @NotNull
    @JsonIgnore
    private String email;

    @NotNull
    @JsonIgnore
    private String profile;

    @NotNull
    @JsonIgnore
    private int status;

    @NotNull
    @JsonIgnore
    private LocalDateTime createAt;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();

}
