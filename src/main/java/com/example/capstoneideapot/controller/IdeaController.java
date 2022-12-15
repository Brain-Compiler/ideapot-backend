package com.example.capstoneideapot.controller;

import com.example.capstoneideapot.entity.Idea;
import com.example.capstoneideapot.entity.dto.ErrorDto;
import com.example.capstoneideapot.entity.dto.idea.IdeaDto;
import com.example.capstoneideapot.entity.dto.idea.IdeaLDto;
import com.example.capstoneideapot.service.idea.IdeaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/idea")
public class IdeaController {

    private final IdeaService ideaService ;

    // GET
    @GetMapping("/{id}")
    public ResponseEntity<Idea> getIdeaById(@PathVariable Long id) {
        return ideaService.getIdeaById(id);
    }

    @GetMapping
    public ResponseEntity<List<IdeaLDto>> getIdeaAll() {
        return ideaService.getIdeaAll();
    }

    // POST
    @PostMapping
    public ResponseEntity<Idea> createIdea(@RequestPart IdeaDto ideaDto,
                               @RequestPart(required = false) List<MultipartFile> files) throws IOException {
        return ideaService.createIdea(ideaDto, files);
    }

    // PUT
    @PutMapping  // this method should contain id
    public ResponseEntity<HttpStatus> editIdea(@RequestPart IdeaDto ideaDto,
                             @RequestPart(required = false) List<MultipartFile> files) throws  IOException {
        return ideaService.editIdea(ideaDto, files);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteIdea(@PathVariable Long id) {
        return ideaService.deleteIdea(id);
    }

    // ELSE

}
