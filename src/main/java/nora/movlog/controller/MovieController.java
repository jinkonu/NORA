package nora.movlog.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nora.movlog.domain.movie.Movie;
import nora.movlog.service.movie.MovieService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/movie")
@Controller
@Slf4j
public class MovieController {
    private final MovieService movieService;

    @GetMapping("/{id}")
    public String movieProfile(@PathVariable String id, Model model) {
        Movie movie = movieService.findOne(id);
        model.addAttribute("movie", movie);

        return "moviePage";
    }
}
