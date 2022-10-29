package com.example.capstoneideapot.entity.dto.competition;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CompetitionDto {

    private Long id;

    private String title;

    private String host;

    private String homepage;

    private String qualification;

    private String startDate;

    private String endDate;

    private Long totalPrice;

    private Long firstPlacePrice;

    private String category1;

    private String category2;

    private String category3;

}
