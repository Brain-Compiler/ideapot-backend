package com.example.capstoneideapot.controller;

import com.example.capstoneideapot.entity.Competition;
import com.example.capstoneideapot.entity.dto.competition.CompetitionDto;
import com.example.capstoneideapot.service.competition.CompetitionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/competition")
public class CompetitionController {

    private final CompetitionService competitionService;

    // GET
    @GetMapping("/{id}")
    public ResponseEntity<Competition> getCompetitionById(@PathVariable Long id) {
        return competitionService.findCompetitionById(id);
    }

    @GetMapping
    public ResponseEntity<List<Competition>> getCompetitionAll() {
        return competitionService.findCompetitionAll();
    }

    // POST
    @PostMapping("/save")
    public ResponseEntity<Competition> saveCompetition(@RequestPart CompetitionDto competitionDto,
                                                       @RequestPart(required = false, name = "poster") MultipartFile poster) throws IOException {
        Competition competition = competitionService.saveCompetition(competitionDto, poster);
        return new ResponseEntity<>(competition, HttpStatus.CREATED);
    }

    // PUT
    @PutMapping("/edit")
    public ResponseEntity<Competition> editCompetiton(@RequestPart CompetitionDto competitionDto,
                                                      @RequestPart(required = false) MultipartFile poster) throws IOException {
        Competition competition = competitionService.saveCompetition(competitionDto, poster);
        return new ResponseEntity<>(competition, HttpStatus.OK);
    }

    // DELETE
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteCompetition(@PathVariable Long id) {
        try {
            competitionService.deleteCompetition(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception exception) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

