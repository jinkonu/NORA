package nora.movlog.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nora.movlog.service.movie.MovieService;
import nora.movlog.service.user.MemberService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    }
}
