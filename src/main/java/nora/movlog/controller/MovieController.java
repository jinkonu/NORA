package nora.movlog.controller;

import lombok.RequiredArgsConstructor;
import nora.movlog.entity.Movie;
import nora.movlog.service.MovieService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/movie")
@RequiredArgsConstructor
@RestController
public class MovieController {
    private final MovieService movieService;

    @GetMapping("/{id}")
    public Movie showMovie(@PathVariable String id) {
        return movieService.findByKobisId(id);
    }
}
