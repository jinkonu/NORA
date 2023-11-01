package nora.movlog.repository;

import nora.movlog.entity.Movie;
import nora.movlog.repository.interfaces.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class MovieJpaRepositoryTest {
    @Autowired MovieRepository movieRepository;
    static String title1 = "펄프 픽션";
    static String title2 = "초록불꽃소년단";

    @Transactional
    @Rollback( value = false )
    @BeforeEach
    void setUp() {
        movieRepository.save(Movie.createMockMovie(title1));
        movieRepository.save(Movie.createMockMovie(title2));
    }

    @Transactional
    @Rollback( value = false )
    @Test
    @DisplayName("DB에 영화 객체 저장")
    void save_DB에_영화_객체_저장() {
    }

    @Transactional( readOnly = true )
    @Test
    @DisplayName("DB ID를 통한 영화 검색")
    void findById_DB_ID를_통한_영화_검색() {
        Movie movie1 = movieRepository.findById((long) 1);
        Movie movie2 = movieRepository.findById((long) 2);

        assertThat(movie1.getTitleKo()).isEqualTo(title1);
        assertThat(movie2.getTitleKo()).isEqualTo(title2);
    }

    @Transactional( readOnly = true )
    @Test
    @DisplayName("파라미터 name의 포함 여부를 활용한 영화 검색")
    void findByName_영화_제목을_활용한_영화_검색() {
        List<Movie> movie1 = movieRepository.findByName("펄프");
        List<Movie> movie2 = movieRepository.findByName("초록");

        assertThat(movie1.get(0).getTitleKo()).isEqualTo(title1);
        assertThat(movie2.get(0).getTitleKo()).isEqualTo(title2);
    }
}