package com.example.capstoneideapot.service.competition;

import com.example.capstoneideapot.entity.Competition;
import com.example.capstoneideapot.entity.dto.competition.CompetitionDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface CompetitionService {

    // GET
    Competition findCompetitionById(Long id);

    List<Competition> findCompetitionAll();

    // POST
    Competition saveCompetition(CompetitionDto competitionDto, MultipartFile poster) throws IOException;

    // PUT

    // DELETE
    void deleteCompetition(Long id);

    // ELSE
    Competition createCompetition(CompetitionDto competitionDto);

}
