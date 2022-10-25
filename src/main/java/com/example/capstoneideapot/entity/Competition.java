package com.example.capstoneideapot.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Competition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String host;

    @NotNull
    private String homepage;

    @NotNull
    private String Qualification;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @NotNull
    private Long totalPrice;

    @NotNull
    private Long firstPlacePrice;

    @OneToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToMany
    private Set<Files> poster = new HashSet<>();

}
