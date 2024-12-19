package com.mrlaksus.projectfinalexam.Service;
import com.mrlaksus.projectfinalexam.Model.Movie;
import com.mrlaksus.projectfinalexam.Repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Movie saveMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    public Movie getMovieById(Long id) {
        return movieRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Movie not found"));
    }
    public void deleteById(Long id) {
        movieRepository.deleteById(id);
    }

}

