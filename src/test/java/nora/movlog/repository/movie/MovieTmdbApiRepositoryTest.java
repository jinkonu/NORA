package nora.movlog.repository.movie;

import com.fasterxml.jackson.databind.JsonNode;
import nora.movlog.domain.movie.WatchGrade;
import nora.movlog.dto.movie.MovieTmdbDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.Iterator;

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
    static Integer genreId = 80;
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
        assertThat(dto.getGenres().keySet()).contains(genreId);
        assertThat(dto.getDirectors().get(directorId)).isEqualTo(director);
        assertThat(dto.getActors().get(actorId)).isEqualTo(actor);
        assertThat(dto.getWatchGrade()).isEqualTo(watchGrade);
    }

    @Test
    @DisplayName("http GET 요청을 통해 JsonNode 생성")
    void mapJsonNode_http_GET_요청을_통해_JsonNode_생성() throws IOException {
        JsonNode node = movieRepository.mapJsonNode(TMDB_SEARCH_BY_ID_PATH + id, "", "");

        String titleFromNode = node.get("title").asText();
        String runtimeFromNode = node.get("runtime").asText();

        assertThat(titleFromNode).isEqualTo(title);
        assertThat(runtimeFromNode).isEqualTo(runTime);
    }
}