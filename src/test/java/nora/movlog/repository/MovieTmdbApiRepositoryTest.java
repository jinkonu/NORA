package nora.movlog.repository;

import com.fasterxml.jackson.databind.JsonNode;
import nora.movlog.domain.movie.WatchGrade;
import nora.movlog.dto.MovieTmdbDto;
import nora.movlog.repository.movie.MovieTmdbApiRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static nora.movlog.domain.constant.StringConstant.TMDB_SEARCH_BY_ID_PATH;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class MovieTmdbApiRepositoryTest {

    @Autowired
    MovieTmdbApiRepository movieRepository;
    static String id = "275";  // 영화 "파고" id
    static String title = "파고";
    static String runTime = "98";
    static String prdtYear = "1996";
    static String nation = "US";
    static String genre = "범죄";
    static String director = "Joel Coen";
    static String directorId = "1223";
    static String actor = "William H. Macy";
    static String actorId = "3905";
    static WatchGrade watchGrade = WatchGrade.ADULT;

    @Test
    @DisplayName("id 기반 tmdb api 검색")
    void findById_id_기반_tmdb_api_검색() throws IOException {
        MovieTmdbDto dto = movieRepository.findById(id);

        assertThat(dto.getTitleKo()).isEqualTo(title);
        assertThat(dto.getRunTime()).isEqualTo(runTime);
        assertThat(dto.getPrdtYear()).isEqualTo(prdtYear);
        assertThat(dto.getNation()).contains(nation);
        assertThat(dto.getGenres()).contains(genre);
        assertThat(dto.getDirectors().get(directorId)).isEqualTo(director);
        assertThat(dto.getActors().get(actorId)).isEqualTo(actor);
        assertThat(dto.getWatchGrade()).isEqualTo(watchGrade);
    }

    @Test
    @DisplayName("http GET 요청을 통해 JsonNode 생성")
    void mapJsonNode_http_GET_요청을_통해_JsonNode_생성() throws IOException {
        String titleFromNode;

        JsonNode node = movieRepository.mapJsonNode(TMDB_SEARCH_BY_ID_PATH + id, "", "");

        titleFromNode = node.get("title").textValue();

        assertThat(titleFromNode).isEqualTo(title);
    }
}