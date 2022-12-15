package com.example.capstoneideapot.service.idea;

import com.example.capstoneideapot.entity.Idea;
import com.example.capstoneideapot.entity.dto.ErrorDto;
import com.example.capstoneideapot.entity.dto.idea.IdeaDto;
import com.example.capstoneideapot.entity.dto.idea.IdeaLDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface IdeaService {

    // GET
    ResponseEntity<Idea> getIdeaById(Long id);

    ResponseEntity<List<IdeaLDto>> getIdeaAll();

    // POST
    ResponseEntity<Idea> createIdea(IdeaDto ideaDto, List<MultipartFile> files) throws IOException;

    // PUT
    ResponseEntity<HttpStatus> editIdea(@RequestBody IdeaDto ideaDto, List<MultipartFile> files) throws IOException;

    // DELETE
    ResponseEntity<HttpStatus> deleteIdea(Long id);

    ResponseEntity<HttpStatus> deleteIdeaFiles(Idea idea);

    // ELSE
    Idea createIdeaEntity(IdeaDto ideaDto);

    void setIdeaDtoToIdea(Idea idea, IdeaDto ideaDto);

}
