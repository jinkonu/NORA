package nora.movlog.service.movie;

import nora.movlog.domain.movie.Movie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static nora.movlog.utils.constant.NumberConstant.*;
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

    @DisplayName("문자열 기반 검색")
    @Rollback(value = false)
    @ValueSource(strings = "보이")
    @ParameterizedTest
    void search_문자열_기반_검색(String searchParam) {

        List<Movie> searchResult = movieService.search(searchParam, DEFAULT_SEARCH_PAGE, DEFAULT_SEARCH_SIZE);
        List<Double> popularities = searchResult.stream()
                .map(movie -> movie.getPopularity())
                .collect(Collectors.toList());
        Collections.reverse(popularities);

        assertThat(searchResult.size()).isGreaterThanOrEqualTo(MIN_SEARCH_LIST_SIZE);
        assertThat(popularities).isSorted();
    }

    @DisplayName("API 검색 결과 영화들을 DB에 저장")
    @Rollback(value = false)
    @ValueSource(strings = "돼지")
    @ParameterizedTest
    void search_API_검색_결과_영화들을_DB에_저장(String searchParam) {

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