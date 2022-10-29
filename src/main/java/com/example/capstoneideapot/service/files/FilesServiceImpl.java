package com.example.capstoneideapot.service.files;

import com.example.capstoneideapot.entity.Competition;
import com.example.capstoneideapot.entity.File;
import com.example.capstoneideapot.entity.Idea;
import com.example.capstoneideapot.repository.CompetitionRepository;
import com.example.capstoneideapot.repository.FilesRepository;
import com.example.capstoneideapot.repository.IdeaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FilesServiceImpl implements FilesService {

    private final IdeaRepository ideaRepository;

    private final CompetitionRepository competitionRepository;

    private final FilesRepository filesRepository;

    private final String path = System.getProperty("user.dir") + "\\src\\main\\resources\\static";

    @Override
    public void saveIdeaAndFiles(Idea idea, List<MultipartFile> files) throws IOException {
        if (files != null) {
            if (!files.get(0).getOriginalFilename().equals("")) {
                String ideaPath = path + "\\idea";
                Set<File> fileEntitySet = new HashSet<>();

                for (MultipartFile file : files) {
                    File fileEntity = new File(saveFileAndReturnFileName(ideaPath, file));

                    log.info("file name: {}", file.getOriginalFilename());

                    fileEntitySet.add(fileEntity);
                }
                addIdeaToFiles(idea, fileEntitySet);

                filesRepository.saveAll(fileEntitySet);
            }
        }
        ideaRepository.save(idea);
    }

    @Override
    public Competition saveCompetitionAndPoster(Competition competition, MultipartFile poster) throws IOException {
        if (competition.getId() != null) {
            deleteCompetitionPoster(competition);
        }

        if (poster != null) {
            if (!poster.getOriginalFilename().equals("")) {
                String competitionPath = path + "\\competition";
                File file = new File(saveFileAndReturnFileName(competitionPath, poster));

                competition.setFiles(file);

                filesRepository.save(file);
            }
        }
        competitionRepository.save(competition);

        return competition;
    }

    @Override
    public void deleteCompetitionPoster(Competition competition) {
        File poster = competition.getFiles();

        if (poster != null) {
            String fileName = poster.getName();
            java.io.File file = new java.io.File(path + "\\competition\\" + fileName);

            competition.setFiles(null);
            filesRepository.delete(poster);

            if (file.exists()) {
                if (!file.delete()) {
                    log.info("파일 삭제 실패");
                } else {
                    log.info("파일 삭제 성공");
                }
            } else {
                log.info("파일이 존재하지 않음");
            }
        } else {
            log.info("Poster is null");
        }
    }

    @Override
    public String saveFileAndReturnFileName(String path, MultipartFile file) throws IOException {
        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + file.getOriginalFilename();
        java.io.File saveFile = new java.io.File(path, fileName);

        file.transferTo(saveFile);
        return fileName;
    }

    @Override
    public void addIdeaToFiles(Idea idea, Set<File> files) {
        if (idea.getFiles() == null) {
            idea.setFiles(files);
        } else {
            idea.getFiles().addAll(files);
        }
    }
}
