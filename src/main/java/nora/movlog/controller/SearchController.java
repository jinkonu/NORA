package nora.movlog.controller;

import lombok.RequiredArgsConstructor;
import nora.movlog.service.MovieService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/*
"Search" 페이지
지금은 searchMovie와 searchMember로 쪼개서 검색하도록
후에 두 가지를 한꺼번에 할 수 있도록 해야할듯
 */

@RequestMapping("/search")
@RequiredArgsConstructor
@RestController
public class SearchController {
    private final MovieService movieService;

    @GetMapping("/movie")
    public HashMap<String, String> searchMovie(@RequestParam String searchDt) {
        return movieService.search(searchDt);
    }
}
