package nora.movlog.repository.movie;

/*
TMDB API에 쿼리 날리는 리포지토리
 */

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nora.movlog.utils.dto.movie.MovieTmdbDto;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static nora.movlog.utils.constant.StringConstant.*;
import static nora.movlog.utils.constant.NumberConstant.*;

@Repository
public class MovieTmdbApiRepository {
    // id 기반으로 TMDB API에 질의해서 dto 제공
    public MovieTmdbDto findById(String id) {
        List<JsonNode> nodes = new ArrayList<>();

        nodes.add(mapJsonNode(TMDB_SEARCH_BY_ID_PATH + id, TMDB_CREDITS_PATH, NO_ARGS));                // basic info + credits info
        nodes.add(mapJsonNode(TMDB_SEARCH_BY_ID_PATH + id + "/" + TMDB_GRADE_PATH, NO_ARGS, NO_ARGS));  // grade info

        return MovieTmdbDto.create(nodes);
    }

    // 문자열 기반으로 TMDB API에 질의해서 dto 제공
    public List<MovieTmdbDto> findByQuery(String query, int dbMoviesSize) {
        List<String> ids = filterPopularity(mapJsonNode(TMDB_SEARCH_BY_QUERY_PATH, NO_ARGS, query).get(JSON_NODE_RESULTS));

        return ids.stream()
                .limit(MAX_SEARCH_LIST_SIZE - dbMoviesSize)
                .map(this::findById)
                .collect(Collectors.toList());
    }

    // API의 'popularity' 항목에 대해 일정 값을 넘지 못하면 검색 결과에서 제외함
    private List<String> filterPopularity(JsonNode results) {
        List<String> ids = new ArrayList<>();

        for (JsonNode node : results)
            if (node.get(JSON_NODE_POPULARITY).asDouble(DEFAULT_POPULARITY) > LEAST_POPULARITY)
                ids.add(node.get(JSON_NODE_ID).asText());

        return ids;
    }

    // API 요청 결과를 JsonNode 객체로 매핑
    public JsonNode mapJsonNode(String path, String append, String query) {
        WebClient client = WebClient.builder()
                .baseUrl(TMDB_URL)
                .defaultHeader(HttpHeaders.AUTHORIZATION, TMDB_KEY)
                .build();

        String result = client.get()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .queryParam(APPEND_TO_RESPONSE, append)
                        .queryParam(QUERY, query)
                        .queryParam(LANGUAGE, LANGUAGE_KOREAN)
                        .queryParam(PAGE, DEFAULT_PAGE)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            return new ObjectMapper().readTree(result);
        } catch (JsonProcessingException e) {
            return null;
        }
    }
}
