package com.example.capstoneideapot.controller;

import com.example.capstoneideapot.entity.Idea;
import com.example.capstoneideapot.entity.dto.ErrorDto;
import com.example.capstoneideapot.entity.dto.idea.IdeaDto;
import com.example.capstoneideapot.service.idea.IdeaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/idea")
public class IdeaController {

    private final IdeaService ideaService;

    // GET
    @GetMapping("/{id}")
    public Idea getIdeaById(@PathVariable Long id) {
        return ideaService.getIdeaById(id);
    }

    @GetMapping
    public List<Idea> getIdeaAll() {
        return ideaService.getIdeaAll();
    }

    // POST
    @PostMapping
    public ErrorDto createIdea(@RequestPart IdeaDto ideaDto,
                               @RequestPart(required = false) List<MultipartFile> files) throws IOException {
        return ideaService.createIdea(ideaDto, files);
    }

    // PUT
    @PutMapping  // this method should contain id
    public ErrorDto editIdea(@RequestPart IdeaDto ideaDto,
                             @RequestPart(required = false) List<MultipartFile> files) throws  IOException {
        return ideaService.editIdea(ideaDto, files);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ErrorDto deleteIdea(@PathVariable Long id) {
        return ideaService.deleteIdea(id);
    }

    // ELSE

}
