package com.example.capstoneideapot.repository;

import com.example.capstoneideapot.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByTagContains(String tag);

}
