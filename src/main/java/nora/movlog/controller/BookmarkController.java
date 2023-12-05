package nora.movlog.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nora.movlog.service.movie.MovieService;
import nora.movlog.service.user.MemberService;
import nora.movlog.utils.MemberFinder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static nora.movlog.utils.constant.StringConstant.BOOKMARK_URI;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(BOOKMARK_URI)
@RestController
public class BookmarkController {

    private final MemberService memberService;
    private final MovieService movieService;


    /* CREATE */
    @PostMapping("/{movieId}/seen/add")
    public void addSeenMovie(@PathVariable String movieId,
                             Authentication auth) {
        memberService.addSeenMovie(MemberFinder.getUsernameFrom(auth), movieId);
    }

    @PostMapping("/{movieId}/toSee/add")
    public void addToSeeMovie(@PathVariable String movieId,
                              Authentication auth) {
        log.info("{} IS ADDED !", movieId);
        memberService.addToSeeMovie(MemberFinder.getUsernameFrom(auth), movieId);
    }


    /* READ */
    @GetMapping("/seen")
    public String readSeenMovie(Model model,
                                Authentication auth) {
        model.addAttribute("movies", movieService.findAllSeenFrom(MemberFinder.getUsernameFrom(auth)));

        return "seenMoviesPage";
    }

    @GetMapping("/toSee")
    public String readToSeeMovies(Model model,
                                  Authentication auth) {
        model.addAttribute("movies", movieService.findAllToSeeFrom(MemberFinder.getUsernameFrom(auth)));

        return "toSeeMoviesPage";
    }
}
