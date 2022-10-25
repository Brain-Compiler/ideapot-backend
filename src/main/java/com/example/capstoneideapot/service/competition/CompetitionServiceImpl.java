package com.example.capstoneideapot.service.competition;

import com.example.capstoneideapot.entity.Competition;
import com.example.capstoneideapot.entity.dto.ErrorDto;
import com.example.capstoneideapot.entity.dto.competition.CompetitionDto;
import com.example.capstoneideapot.repository.CompetitionRepository;
import com.example.capstoneideapot.service.files.FilesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompetitionServiceImpl implements CompetitionService {

    private final FilesService filesService;

    private final CompetitionRepository competitionRepository;

    @Override
    public Competition findCompetitionById(Long id) {
        return competitionRepository.findById(id).orElse(null);
    }

    @Override
    public List<Competition> findCompetitionAll() {
        return competitionRepository.findAll();
    }

    @Override
    public ErrorDto saveCompetition(CompetitionDto competitionDto, MultipartFile poster) throws IOException {
        ErrorDto error = new ErrorDto("없음");
        Competition competition = createCompetition(competitionDto);

        filesService.saveCompetitionAndFiles(competition, poster);

        return error;
    }

    @Override
    public Competition createCompetition(CompetitionDto competitionDto) {
        Competition competition = new Competition();
        competition.setTitle(competition.getTitle());
        competition.setHost(competition.getHost());
        competition.setHomepage(competition.getHomepage());
        competition.setQualification(competition.getQualification());
        competition.setStartDate(competition.getStartDate());
        competition.setEndDate(competition.getEndDate());
        competition.setTotalPrice(competition.getTotalPrice());
        competition.setFirstPlacePrice(competition.getFirstPlacePrice());

        return competition;
    }
}
