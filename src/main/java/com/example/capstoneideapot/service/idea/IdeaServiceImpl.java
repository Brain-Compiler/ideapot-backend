package com.example.capstoneideapot.service.idea;

import com.example.capstoneideapot.entity.Files;
import com.example.capstoneideapot.entity.Idea;
import com.example.capstoneideapot.entity.User;
import com.example.capstoneideapot.entity.dto.ErrorDto;
import com.example.capstoneideapot.entity.dto.idea.IdeaDto;
import com.example.capstoneideapot.repository.FilesRepository;
import com.example.capstoneideapot.repository.IdeaRepository;
import com.example.capstoneideapot.repository.UserRepository;
import com.example.capstoneideapot.service.files.FilesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class IdeaServiceImpl implements IdeaService {

    private final UserRepository userRepository;

    private final IdeaRepository ideaRepository;

    private final FilesRepository filesRepository;

    private final FilesService filesService;

    @Override
    public Idea getIdeaById(Long id) {
        return ideaRepository.findById(id).orElse(null);
    }

    @Override
    public List<Idea> getIdeaAll() {
        return ideaRepository.findAll();
    }

    @Override
    public ErrorDto createIdea(IdeaDto ideaDto, List<MultipartFile> files) {
        ErrorDto error = new ErrorDto("없음");

        try {
            Idea idea = createIdeaEntity(ideaDto);

            filesService.saveIdeaAndFiles(idea, files);
        } catch (Exception exception) {
            error.setError("오류");
            log.info(exception.getMessage());
        }
        return error;
    }

    @Override
    public ErrorDto editIdea(IdeaDto ideaDto, List<MultipartFile> files) throws IOException {
        ErrorDto error = new ErrorDto("없음");
        Long ideaID = ideaDto.getId();
        Idea idea = ideaRepository.findById(ideaID).orElse(null);

        if (idea != null) {
            setIdeaDtoToIdea(idea, ideaDto);
            if (files != null) {
                error = deleteIdeaFiles(idea);
                filesService.saveIdeaAndFiles(idea, files);
            }
        } else {
            return new ErrorDto("게시물이 존재하지 않음");
        }
        return error;
    }

    @Override
    public ErrorDto deleteIdea(Long id) {
        Idea idea = ideaRepository.findById(id).orElse(null);

        if (idea != null) {
            ErrorDto error = deleteIdeaFiles(idea);
            ideaRepository.delete(idea);
            return error;
        } else {
            return new ErrorDto("게시글이 존재하지 않음");
        }
    }

    @Override
    public ErrorDto deleteIdeaFiles(Idea idea) {
        ErrorDto error = new ErrorDto("없음");
        Set<Files> ideaFiles = idea.getFiles();

        idea.setFiles(null);

        if (ideaFiles != null) {
            for (Files ideaFile : ideaFiles) {
                String fileName = ideaFile.getName();
                File file = new File(System.getProperty("user.dir") + "\\src\\main\\resources\\static\\idea\\" + fileName);

                filesRepository.delete(ideaFile);

                if (file.exists()) {
                    if (!file.delete()) {
                        error.setError("파일 삭제 실패");
                    }
                } else {
                    error.setError("파일이 존재하지 않음");
                }
            }
        }
        return error;
    }

    @Override
    public Idea createIdeaEntity(IdeaDto ideaDto) {
        Long userId = ideaDto.getUserId();
        User user = userRepository.findById(userId).orElse(null);

        Idea idea = new Idea();
        idea.setUser(user);
        idea.setTitle(ideaDto.getTitle());
        idea.setDescription(ideaDto.getDescription());
        idea.setPrice(ideaDto.getPrice());
        idea.setStatus(0);
        idea.setCreatedAt(LocalDateTime.now());
        return idea;
    }

    @Override
    public void setIdeaDtoToIdea(Idea idea, IdeaDto ideaDto) {
        idea.setTitle(ideaDto.getTitle());
        idea.setDescription(ideaDto.getDescription());
        idea.setPrice(ideaDto.getPrice());
        idea.setEditAt(LocalDateTime.now());
    }

}
