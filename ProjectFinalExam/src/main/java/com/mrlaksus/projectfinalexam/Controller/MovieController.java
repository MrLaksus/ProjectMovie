package com.mrlaksus.projectfinalexam.Controller;

import com.mrlaksus.projectfinalexam.Model.Movie;
import com.mrlaksus.projectfinalexam.Service.MovieService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/movies")
public class MovieController {
    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping
    public List<Movie> getAllMovies() {
        return movieService.getAllMovies();
    }

    @PostMapping
    public String saveMovie(@ModelAttribute Movie movie, @RequestParam("image") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String uploadDir = "src/main/resources/static/images";

            // Создаём папку, если её нет
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Сохраняем файл
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Устанавливаем URL изображения
            movie.setImageUrl("/images/" + fileName);
        }
        movieService.saveMovie(movie);
        return "redirect:/movies/list";
    }

    @DeleteMapping("/{id}")
    public String deleteMovie(@PathVariable Long id) throws IOException {
        Movie movie = movieService.getMovieById(id);
        if (movie.getImageUrl() != null) {
            String imagePath = "src/main/resources/static" + movie.getImageUrl();
            Files.deleteIfExists(Paths.get(imagePath)); // Удаление файла
        }
        movieService.deleteById(id);
        return "redirect:/movies/list";
    }

    @GetMapping("/{id}")
    public Movie getMovieById(@PathVariable Long id) {
        return movieService.getMovieById(id);
    }

    @GetMapping("/new")
    public String newMovieForm(Model model) {
        model.addAttribute("movie", new Movie());
        return "new";
    }

    @GetMapping("/list")
    public String showMovieList(Model model) {
        model.addAttribute("movies", movieService.getAllMovies());
        return "index";
    }

    @PutMapping("/{id}")
    public String updateMovie(@PathVariable Long id,
                              @ModelAttribute Movie updatedMovie,
                              @RequestParam(value = "image", required = false) MultipartFile file) throws IOException {
        Movie existingMovie = movieService.getMovieById(id);

        // Обновляем поля
        existingMovie.setTitle(updatedMovie.getTitle());
        existingMovie.setDescription(updatedMovie.getDescription());
        existingMovie.setGenre(updatedMovie.getGenre());
        existingMovie.setRating(updatedMovie.getRating());

        // Проверяем, был ли загружен новый файл изображения
        if (file != null && !file.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String uploadDir = "src/main/resources/static/images";

            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            existingMovie.setImageUrl("/images/" + fileName);
        }

        movieService.saveMovie(existingMovie);
        return "redirect:/movies/list";
    }

    @GetMapping("/{id}/edit")
    public String editMovieForm(@PathVariable Long id, Model model) {
        Movie movie = movieService.getMovieById(id);
        model.addAttribute("movie", movie);
        return "new";  // Страница редактирования должна быть в папке /src/main/resources/templates
    }
}
