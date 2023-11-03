package nora.movlog.repository;

import nora.movlog.domain.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class MovieJpaRepositoryTest {

    @Autowired MovieJpaRepository movieRepository;
    static String id1 = "19940315";
    static String title1 = "펄프 픽션";
    static String id2 = "19960051";
    static String title2 = "돼지가 우물에 빠진 날";

    @Transactional
    @Rollback( value = true )
    @BeforeEach
    void setUp() {
        movieRepository.save(Movie.createMockMovie(id1, title1));
        movieRepository.save(Movie.createMockMovie(id2, title2));
    }

    @Transactional( readOnly = true )
    @Test
    @DisplayName("DB_ID를_통한_영화_검색")
    void findById_DB_ID를_통한_영화_검색() {
        Movie movie1 = movieRepository.findById(id1);
        Movie movie2 = movieRepository.findById(id2);

        assertThat(movie1.getTitleKo()).isEqualTo(title1);
        assertThat(movie2.getTitleKo()).isEqualTo(title2);
    }

    @Transactional( readOnly = false )
    @Test
    @DisplayName("영화_제목을_활용한_영화_검색")
    void findByName_영화_제목을_활용한_영화_검색() {
        movieRepository.flush();

        List<Movie> movie1 = movieRepository.findByName("펄프 픽션");
        List<Movie> movie2 = movieRepository.findByName("돼지가 우물에 빠진 날");

        assertThat(movie1.get(0).getTitleKo()).isEqualTo(title1);
        assertThat(movie2.get(0).getTitleKo()).isEqualTo(title2);
    }
}