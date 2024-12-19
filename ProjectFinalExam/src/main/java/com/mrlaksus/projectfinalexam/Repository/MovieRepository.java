package com.mrlaksus.projectfinalexam.Repository;

import com.mrlaksus.projectfinalexam.Model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}