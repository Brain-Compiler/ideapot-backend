package com.example.capstoneideapot.service.idea;

import com.example.capstoneideapot.entity.Idea;
import com.example.capstoneideapot.entity.dto.ErrorDto;
import com.example.capstoneideapot.entity.dto.idea.IdeaDto;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public interface IdeaService {

    // GET
    Idea getIdeaById(Long id);

    List<Idea> getIdeaAll();

    // POST
    ErrorDto createIdea(IdeaDto ideaDto, List<MultipartFile> files) throws IOException;

    // PUT
    ErrorDto editIdea(@RequestBody IdeaDto ideaDto, List<MultipartFile> files) throws IOException;

    // DELETE
    ErrorDto deleteIdea(Long id);

    ErrorDto deleteIdeaFiles(Idea idea);

    // ELSE
    Idea createIdeaEntity(IdeaDto ideaDto);

    void setIdeaDtoToIdea(Idea idea, IdeaDto ideaDto);

}
