package com.example.capstoneideapot.controller;

import com.example.capstoneideapot.entity.Competition;
import com.example.capstoneideapot.entity.dto.ErrorDto;
import com.example.capstoneideapot.entity.dto.competition.CompetitionDto;
import com.example.capstoneideapot.service.competition.CompetitionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/competition")
@RequiredArgsConstructor
public class CompetitionController {

    private final CompetitionService competitionService;

    // GET
    @GetMapping("/{id}")
    public Competition getCompetitionById(@PathVariable Long id) {
        return competitionService.findCompetitionById(id);
    }

    @GetMapping
    public List<Competition> getCompetitionAll() {
        return competitionService.findCompetitionAll();
    }

    // POST
    @PostMapping("/save")
    public ErrorDto saveCompetition(@RequestPart CompetitionDto competitionDto,
                                    @RequestPart(required = false) MultipartFile poster) throws IOException {
        return competitionService.saveCompetition(competitionDto, poster);
    }

    @PutMapping
    public void editCompetiton(@RequestPart CompetitionDto competitionDto,
                               @RequestPart(required = false) List<MultipartFile> poster) {

    }

}
