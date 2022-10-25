package com.example.capstoneideapot.service.files;

import com.example.capstoneideapot.entity.Competition;
import com.example.capstoneideapot.entity.Files;
import com.example.capstoneideapot.entity.Idea;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Service
public interface FilesService {

    // GET

    // POST
    void saveIdeaAndFiles(Idea idea, List<MultipartFile> files) throws IOException;

    void saveCompetitionAndFiles(Competition competition, MultipartFile poster) throws IOException;

    // PUT

    // DELETE

    // ELSE
    void saveFile(String path, MultipartFile file) throws IOException;

    String saveFileAndReturnFileName(String path, MultipartFile file) throws IOException;

    void addIdeaToFiles(Idea idea, Set<Files> files);

}
