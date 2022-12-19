package com.example.capstoneideapot.service.idea;

import com.example.capstoneideapot.entity.File;
import com.example.capstoneideapot.entity.Idea;
import com.example.capstoneideapot.entity.User;
import com.example.capstoneideapot.entity.dto.ErrorDto;
import com.example.capstoneideapot.entity.dto.idea.IdeaDto;
import com.example.capstoneideapot.entity.dto.idea.IdeaLDto;
import com.example.capstoneideapot.repository.FilesRepository;
import com.example.capstoneideapot.repository.IdeaRepository;
import com.example.capstoneideapot.repository.UserRepository;
import com.example.capstoneideapot.service.files.FilesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    public ResponseEntity<IdeaLDto> getIdeaById(Long id) {
        Optional<Idea> idea = ideaRepository.findById(id);

        if (idea.isPresent()) {
            IdeaLDto ideaLDto = IdeaLDto.builder()
                    .id(idea.get().getId())
                    .user(idea.get().getUser())
                    .title(idea.get().getTitle())
                    .description(idea.get().getDescription())
                    .price(new DecimalFormat("###,###,###,###").format(idea.get().getPrice()) + "￦")
                    .status(idea.get().getStatus())
                    .createdAt(idea.get().getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH")) + "h")
                    .files(idea.get().getFiles())
                    .build();
            return new ResponseEntity<>(ideaLDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<List<IdeaLDto>> getIdeaAll() {
        List<Idea> ideaList = ideaRepository.findAll();
        List<IdeaLDto> ideaLDtos = new ArrayList<>();

        if (ideaList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        for (Idea idea : ideaList) {
            IdeaLDto ideaLDto = IdeaLDto.builder()
                    .id(idea.getId())
                    .user(idea.getUser())
                    .title(idea.getTitle())
                    .description(idea.getDescription())
                    .price(new DecimalFormat("###,###,###,###").format(idea.getPrice()) + "￦")
                    .status(idea.getStatus())
                    .createdAt(idea.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH")) + "h")
                    .files(idea.getFiles())
                    .build();

            ideaLDtos.add(ideaLDto);
        }
        return new ResponseEntity<>(ideaLDtos, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Idea> createIdea(IdeaDto ideaDto, List<MultipartFile> files) {
        try {
            Idea idea = createIdeaEntity(ideaDto);

            filesService.saveIdeaAndFiles(idea, files);
            return new ResponseEntity<>(idea, HttpStatus.OK);
        } catch (Exception exception) {
            log.info("error: {}", exception.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<HttpStatus> editIdea(IdeaDto ideaDto, List<MultipartFile> files) {
        try {
            Long ideaID = ideaDto.getId();
            Idea idea = ideaRepository.findById(ideaID).orElse(null);

            if (idea != null) {
                setIdeaDtoToIdea(idea, ideaDto);

                if (files != null) {
                    ResponseEntity<HttpStatus> response = deleteIdeaFiles(idea);
                    filesService.saveIdeaAndFiles(idea, files);

                    return response;
                } else {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            log.info("error: {}", exception.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<HttpStatus> deleteIdea(Long id) {
        Idea idea = ideaRepository.findById(id).orElse(null);

        if (idea != null) {
            ResponseEntity<HttpStatus> response = deleteIdeaFiles(idea);
            ideaRepository.delete(idea);

            return response;
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<HttpStatus> deleteIdeaFiles(Idea idea) {
        Set<File> ideaFiles = idea.getFiles();

        idea.setFiles(null);

        if (ideaFiles != null) {
            for (File ideaFile : ideaFiles) {
                String fileName = ideaFile.getName();
                java.io.File file = new java.io.File(System.getProperty("user.dir") + "\\src\\main\\resources\\static\\idea\\" + fileName);

                filesRepository.delete(ideaFile);

                if (file.exists()) {
                    if (!file.delete()) {
                        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
                    }
                } else {
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
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
