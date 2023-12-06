package nora.movlog.controller.movie;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nora.movlog.service.movie.MovieService;
import nora.movlog.service.user.MemberService;
import nora.movlog.service.user.PostService;
import nora.movlog.utils.MemberFinder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static nora.movlog.utils.constant.StringConstant.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(MOVIE_URI)
@Controller
public class MovieController {

    private final MovieService movieService;
    private final MemberService memberService;
    private final PostService postService;


    // 영화 프로필
    @GetMapping(ID_URI)
    public String movieProfile(@PathVariable String id,
                               @RequestParam(defaultValue = DEFAULT_SEARCH_PAGE) int page,
                               @RequestParam(defaultValue = DEFAULT_SEARCH_SIZE) int size,
                               Authentication auth,
                               Model model) {
        model.addAttribute("movie", movieService.findOne(id));
        model.addAttribute("loginMember", memberService.findByLoginId(MemberFinder.getLoginId(auth)));
        model.addAttribute("posts", postService.findAllFromMovie(id, page, size));

        return "moviePage";
    }


    // 북마크 이미 본 영화 확인
    @ResponseBody
    @GetMapping(ID_URI + "/isSeen")
    public boolean isSeen(@PathVariable String id,
                          Authentication auth) {
        return movieService.isSeenFrom(id, MemberFinder.getLoginId(auth));
    }


    // 북마크 보고 싶은 영화 확인
    @ResponseBody
    @GetMapping(ID_URI + "/isSeen")
    public boolean isToSee(@PathVariable String id,
                          Authentication auth) {
        return movieService.isToSeeFrom(id, MemberFinder.getLoginId(auth));
    }
}
