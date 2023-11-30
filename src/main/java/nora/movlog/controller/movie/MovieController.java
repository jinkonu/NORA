package nora.movlog.controller.movie;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nora.movlog.domain.user.PrincipalDetails;
import nora.movlog.service.movie.MovieService;
import nora.movlog.service.user.MemberService;
import nora.movlog.utils.MemberFinder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import static nora.movlog.utils.constant.StringConstant.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(MOVIE_URI)
@Controller
public class MovieController {

    private final MovieService movieService;
    private final MemberService memberService;



    // 영화 프로필
    @GetMapping(ID_URI)
    public String movieProfile(@PathVariable String id,
                               Authentication auth,
                               Model model) {
        model.addAttribute("movie", movieService.findOne(id));
        model.addAttribute("loginMember", memberService.findByLoginId(MemberFinder.getUsernameFrom(auth)));

        return "moviePage";
    }
}
