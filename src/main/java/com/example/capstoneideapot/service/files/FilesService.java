package com.example.capstoneideapot.service.files;

import com.example.capstoneideapot.entity.Competition;
import com.example.capstoneideapot.entity.File;
import com.example.capstoneideapot.entity.Idea;
import com.example.capstoneideapot.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Service
public interface FilesService {

    // GET

    // POST
    User saveUserAndProfile(User user, MultipartFile profile) throws IOException;

    void saveIdeaAndFiles(Idea idea, List<MultipartFile> files) throws IOException;

    Competition saveCompetitionAndPoster(Competition competition, MultipartFile poster) throws IOException;

    // PUT

    // DELETE
    void deleteCompetitionPoster(Competition competition);

    // ELSE
    String saveFileAndReturnFileName(String path, MultipartFile file) throws IOException;

    void addIdeaToFiles(Idea idea, List<File> files);

}
