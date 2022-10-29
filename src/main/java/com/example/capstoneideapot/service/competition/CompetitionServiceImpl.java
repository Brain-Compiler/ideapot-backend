package com.example.capstoneideapot.service.competition;

import com.example.capstoneideapot.entity.Competition;
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
    public Competition saveCompetition(CompetitionDto competitionDto, MultipartFile poster) throws IOException {
        Competition competition;

        if (competitionDto.getId() == null) {
            competition = createCompetition(competitionDto);
        } else {
            Long id = competitionDto.getId();
            competition = competitionRepository.findById(id).orElse(null);
        }
        competition = filesService.saveCompetitionAndPoster(competition, poster);

        return competition;
    }

    @Override
    public void deleteCompetition(Long id) {
        Competition competition = competitionRepository.findById(id).orElse(null);

        if (competition != null) {
            filesService.deleteCompetitionPoster(competition);
            competitionRepository.delete(competition);
        } else {
            log.info("competition is null");
        }
    }

    @Override
    public Competition createCompetition(CompetitionDto competitionDto) {
        Competition competition = new Competition();

        if (competitionDto.getId() != null) {
            competition.setId(competitionDto.getId());
        }
        competition.setTitle(competitionDto.getTitle());
        competition.setHost(competitionDto.getHost());
        competition.setHomepage(competitionDto.getHomepage());
        competition.setQualification(competitionDto.getQualification());
        competition.setStartDate(competitionDto.getStartDate());
        competition.setEndDate(competitionDto.getEndDate());
        competition.setTotalPrice(competitionDto.getTotalPrice());
        competition.setFirstPlacePrice(competitionDto.getFirstPlacePrice());

        return competition;
    }
}
