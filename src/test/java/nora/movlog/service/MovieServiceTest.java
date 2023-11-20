package nora.movlog.service;

import nora.movlog.domain.movie.Movie;
import nora.movlog.service.movie.MovieService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static nora.movlog.domain.constant.NumberConstant.*;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("ALL")
@Transactional
@ExtendWith(SpringExtension.class)
@SpringBootTest
class MovieServiceTest {

    private static final int DEFAULT_SEARCH_PAGE = 0;
    private static final int DEFAULT_SEARCH_SIZE = 5;

    @Autowired
    MovieService movieService;

    @Rollback( value = false )
    @Test
    @DisplayName("문자열 기반 검색")
    void search_문자열_기반_검색() {
        String searchParam = "보이";

        List<Movie> searchResult = movieService.search(searchParam, DEFAULT_SEARCH_PAGE, DEFAULT_SEARCH_SIZE);

        for (Movie movie : searchResult) {
            System.out.println("movie.getTitleKo() = " + movie.getTitleKo());
        }

        assertThat(searchResult.size()).isGreaterThanOrEqualTo(MIN_SEARCH_LIST_SIZE);
    }

    @Rollback( value = false )
    @Test
    @DisplayName("API 검색 결과 영화들을 DB에 저장")
    void search_API_검색_결과_영화들을_DB에_저장() {
        String searchParam = "돼지";

        List<Movie> searchResult = movieService.search(searchParam, DEFAULT_SEARCH_PAGE, DEFAULT_SEARCH_SIZE);
        List<Movie> dbMovieList = new ArrayList<>();

        for (Movie movie : searchResult)
            dbMovieList.add(movieService.findOne(movie.getId()));

        List<Movie> duplicate = searchResult.stream()
                .filter(dbMovieList::contains)
                .toList();

        assertThat(duplicate.size()).isEqualTo(searchResult.size());
    }
}