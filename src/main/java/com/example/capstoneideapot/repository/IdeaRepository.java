package com.example.capstoneideapot.repository;

import com.example.capstoneideapot.entity.Category;
import com.example.capstoneideapot.entity.Idea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IdeaRepository extends JpaRepository<Idea, Long> {

    List<Idea> findAllByCategory(Category category);

}
