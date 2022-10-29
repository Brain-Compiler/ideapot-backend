package com.example.capstoneideapot.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@ToString
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
    private String startDate;

    @NotNull
    private String endDate;

    @NotNull
    private Long totalPrice;

    @NotNull
    private Long firstPlacePrice;

    @OneToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToOne(cascade = CascadeType.ALL)
    private Files files;

}
