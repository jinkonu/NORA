package nora.movlog.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nora.movlog.entity.Movie;
import nora.movlog.service.MovieService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;

/*
"Search" 페이지
지금은 searchMovie와 searchMember로 쪼개서 검색하도록
후에 두 가지를 한꺼번에 할 수 있도록 해야할듯
 */

@RequiredArgsConstructor
@RequestMapping("/search")
@Controller
@Slf4j
public class SearchController {
    private final MovieService movieService;

    @GetMapping()
    public String search(@RequestParam(value = "query", required = false) String query, Model model) {
        Movie searchResult = movieService.search(query);

        if (!query.isEmpty())
            model.addAttribute(searchResult);

        /* for test */
        System.out.println("searchResult = " + searchResult);

        return "searchForm";
    }
}