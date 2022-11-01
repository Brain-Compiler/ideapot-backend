package com.example.capstoneideapot.repository;

import com.example.capstoneideapot.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilesRepository extends JpaRepository<File, Long> {

    File findByName(String name);

}
