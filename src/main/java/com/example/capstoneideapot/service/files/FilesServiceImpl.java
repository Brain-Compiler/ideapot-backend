package com.example.capstoneideapot.service.files;

import com.example.capstoneideapot.entity.Competition;
import com.example.capstoneideapot.entity.Files;
import com.example.capstoneideapot.entity.Idea;
import com.example.capstoneideapot.repository.FilesRepository;
import com.example.capstoneideapot.repository.IdeaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class FilesServiceImpl implements FilesService {

    private final IdeaRepository ideaRepository;

    private final FilesRepository filesRepository;

    @Override
    public void saveIdeaAndFiles(Idea idea, List<MultipartFile> files) throws IOException {
        String path = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\idea";
        Set<Files> fileEntitySet = new HashSet<>();

        for (MultipartFile file : files) {
            Files fileEntity = new Files(saveFileAndReturnFileName(path, file));

            fileEntitySet.add(fileEntity);
        }
        addIdeaToFiles(idea, fileEntitySet);

        ideaRepository.save(idea);
        filesRepository.saveAll(fileEntitySet);
    }

    @Override
    public void saveCompetitionAndFiles(Competition competition, MultipartFile poster) throws IOException {
        String path = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\competition";
        File file = new File(saveFileAndReturnFileName(path, poster));



    }

    @Override
    public void saveFile(String path, MultipartFile file) throws IOException {
        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + file.getOriginalFilename();
        File saveFile = new File(path, fileName);

        file.transferTo(saveFile);
    }

    @Override
    public String saveFileAndReturnFileName(String path, MultipartFile file) throws IOException {
        UUID uuid = UUID.randomUUID();
        String fileName = uuid + "_" + file.getOriginalFilename();
        File saveFile = new File(path, fileName);

        file.transferTo(saveFile);
        return fileName;
    }

    @Override
    public void addIdeaToFiles(Idea idea, Set<Files> files) {
        if (idea.getFiles() == null) {
            idea.setFiles(files);
        } else {
            idea.getFiles().addAll(files);
        }
    }
}
