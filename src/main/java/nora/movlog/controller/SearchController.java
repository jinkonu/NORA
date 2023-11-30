package nora.movlog.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nora.movlog.service.movie.MovieService;
import nora.movlog.service.user.MemberService;
import nora.movlog.utils.MemberFinder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static nora.movlog.utils.constant.StringConstant.*;

/*
"Search" 페이지
지금은 searchMovie와 searchMember로 쪼개서 검색하도록
후에 두 가지를 한꺼번에 할 수 있도록 해야할듯
 */

@RequiredArgsConstructor
@RequestMapping(SEARCH_URI)
@Slf4j
@Controller
public class SearchController {

    private final MovieService movieService;
    private final MemberService memberService;



    // 검색
    @GetMapping()
    public String search(@RequestParam(value = "movieQuery", required = false) String movieQuery,
                         @RequestParam(value = "memberQuery", required = false) String memberQuery,
                         Authentication auth,
                         Model model,
                         @RequestParam(defaultValue = DEFAULT_SEARCH_PAGE) int page,
                         @RequestParam(defaultValue = DEFAULT_SEARCH_SIZE) int size) {
        if (movieQuery != null)
            model.addAttribute("movies", movieService.search(movieQuery, page, size));

        if (memberQuery != null)
            model.addAttribute("members", memberService.findAllByNickname(movieQuery, page, size));

        model.addAttribute("loginMember", memberService.findByLoginId(MemberFinder.getUsernameFrom(auth)));

        return "searchForm";
    }
}
