package nora.movlog.repository;

import nora.movlog.entity.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MovieJpaRepositoryTest {
    @Autowired MovieJpaRepository movieRepository;
    static String title1 = "펄프 픽션";
    static String title2 = "초록불꽃소년단";

    @BeforeEach
    void setUp() {
        movieRepository.save(Movie.createMockMovie(title1));
        movieRepository.save(Movie.createMockMovie(title2));
    }

    @Test
    @DisplayName("DB ID를 통한 영화 검색")
    void findById_DB_ID를_통한_영화_검색() {
        Movie movie1 = movieRepository.findById((long) 1);
        Movie movie2 = movieRepository.findById((long) 2);

        assertThat(movie1.getTitleKo()).isEqualTo(title1);
        assertThat(movie2.getTitleKo()).isEqualTo(title2);
    }

    @Test
    @DisplayName("파라미터 name의 포함 여부를 활용한 영화 검색")
    void findByName_영화_제목을_활용한_영화_검색() {
        List<Movie> movie1 = movieRepository.findByName("펄프");
        List<Movie> movie2 = movieRepository.findByName("초록");

        assertThat(movie1.get(0).getTitleKo()).isEqualTo(title1);
        assertThat(movie2.get(0).getTitleKo()).isEqualTo(title2);
    }
}