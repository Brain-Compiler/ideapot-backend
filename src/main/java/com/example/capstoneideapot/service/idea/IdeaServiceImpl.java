package com.example.capstoneideapot.service.idea;

import com.example.capstoneideapot.entity.Category;
import com.example.capstoneideapot.entity.File;
import com.example.capstoneideapot.entity.Idea;
import com.example.capstoneideapot.entity.User;
import com.example.capstoneideapot.entity.dto.idea.IdeaDto;
import com.example.capstoneideapot.entity.dto.idea.IdeaLDto;
import com.example.capstoneideapot.repository.CategoryRepository;
import com.example.capstoneideapot.repository.FilesRepository;
import com.example.capstoneideapot.repository.IdeaRepository;
import com.example.capstoneideapot.repository.UserRepository;
import com.example.capstoneideapot.service.files.FilesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class IdeaServiceImpl implements IdeaService {

    private final UserRepository userRepository;

    private final IdeaRepository ideaRepository;

    private final FilesRepository filesRepository;

    private final FilesService filesService;

    private final CategoryRepository categoryRepository;

    @Override
    public ResponseEntity<List<Map<Long, String>>> getCategoryList() {
        try {
            List<Map<Long, String>> categoryList = new ArrayList<>();
            List<Category> allCategory = categoryRepository.findAll();

            for (Category category : allCategory) {
                String[] categoryName = category.getTag().split("/");
                categoryList.add(Map.of(category.getId(), categoryName[1]));
            }
            return new ResponseEntity<>(categoryList, HttpStatus.OK);
        } catch (Exception exception) {
            log.info("error: {}", exception.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Override
    public ResponseEntity<List<IdeaLDto>> getIdeaByCategory(Long id) {
        Optional<Category> category = categoryRepository.findById(id);

        if (category.isPresent()) {
            List<Idea> ideaList = ideaRepository.findAllByCategory(category.get());

            List<IdeaLDto> ideaLDtos = setIdeaToIdeaDto(ideaList);
            if (!ideaList.isEmpty()) {
                return new ResponseEntity<>(ideaLDtos, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<IdeaLDto> getIdeaById(Long id) {
        Optional<Idea> idea = ideaRepository.findById(id);

        if (idea.isPresent()) {
            IdeaLDto ideaLDto = IdeaLDto.builder().id(idea.get().getId()).user(idea.get().getUser()).title(idea.get().getTitle()).category(idea.get().getCategory()).description(idea.get().getDescription()).price(new DecimalFormat("###,###,###,###").format(idea.get().getPrice()) + "￦").status(idea.get().getStatus()).createdAt(idea.get().getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH")) + "h").files(idea.get().getFiles()).build();
            return new ResponseEntity<>(ideaLDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Override
    public ResponseEntity<List<IdeaLDto>> getIdeaAll() {
        List<Idea> ideaList = ideaRepository.findAll();

        if (ideaList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        List<IdeaLDto> ideaLDtos = setIdeaToIdeaDto(ideaList);

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
        List<File> ideaFiles = idea.getFiles();

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
        String tag = ideaDto.getCategory1();

        if (ideaDto.getCategory2() != null) {
            tag += "/" + ideaDto.getCategory2();
        }

        if (ideaDto.getCategory2() != null && ideaDto.getCategory3() != null) {
            tag += "/" + ideaDto.getCategory3();
        }

        Category category = categoryRepository.findByTagContains(tag);

        Idea idea = Idea.builder()
                .user(user)
                .title(ideaDto.getTitle())
                .description(ideaDto.getDescription())
                .price(ideaDto.getPrice())
                .category(category)
                .status(0)
                .createdAt(LocalDateTime.now())
                .build();

        return idea;
    }

    @Override
    public void setIdeaDtoToIdea(Idea idea, IdeaDto ideaDto) {
        idea.setTitle(ideaDto.getTitle());
        idea.setDescription(ideaDto.getDescription());
        idea.setPrice(ideaDto.getPrice());
        idea.setEditAt(LocalDateTime.now());
    }

    @Override
    public List<IdeaLDto> setIdeaToIdeaDto(List<Idea> ideaList) {
        List<IdeaLDto> ideaLDtos = new ArrayList<>();

        Comparator<File> cp = new Comparator<File>() {
            @Override
            public int compare(File data1, File data2) {
                Long dataId1 = data1.getId();
                Long dataId2 = data2.getId();

                if (dataId1 > dataId2) {
                    return 1;
                } else {
                    return -1;
                }
            }
        };

        for (Idea idea : ideaList) {
            IdeaLDto ideaLDto = IdeaLDto.builder()
                    .id(idea.getId())
                    .user(idea.getUser())
                    .title(idea.getTitle())
                    .description(idea.getDescription())
                    .category(idea.getCategory())
                    .price(new DecimalFormat("###,###,###,###").format(idea.getPrice()) + "￦")
                    .status(idea.getStatus())
                    .createdAt(idea.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH")) + "h")
                    .build();

            List<File> ideaFiles = idea.getFiles();
            Collections.sort(ideaFiles, cp);

            ideaLDto.setFiles(ideaFiles);

            ideaLDtos.add(ideaLDto);
        }
        return ideaLDtos;
    }
}
