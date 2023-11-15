package nora.movlog.repository.movie;

/*
TMDB API에 쿼리 날리는 리포지토리
 */

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nora.movlog.dto.movie.MovieTmdbDto;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.parseDouble;
import static nora.movlog.domain.constant.StringConstant.*;
import static nora.movlog.domain.constant.NumberConstant.*;

@Repository
public class MovieTmdbApiRepository {
    // id 기반으로 TMDB API에 질의해서 dto 제공
    public MovieTmdbDto findById(String id) throws JsonProcessingException {
        List<JsonNode> nodes = new ArrayList<>();

        nodes.add(mapJsonNode(TMDB_SEARCH_BY_ID_PATH + id, TMDB_CREDITS_PATH, NO_ARGS));                // basic info + credits info
        nodes.add(mapJsonNode(TMDB_SEARCH_BY_ID_PATH + id + "/" + TMDB_GRADE_PATH, NO_ARGS, NO_ARGS));  // grade info

        return MovieTmdbDto.create(nodes);
    }

    // 문자열 기반으로 TMDB API에 질의해서 dto 제공
    public List<MovieTmdbDto> findByQuery(String query) throws JsonProcessingException {
        List<MovieTmdbDto> dtos = new ArrayList<>();
        List<String> ids = filterPopularity(mapJsonNode(TMDB_SEARCH_BY_QUERY_PATH, NO_ARGS, query).get("results"));

        for (String id : ids)
            dtos.add(findById(id));

        return dtos;
    }

    private List<String> filterPopularity(JsonNode results) {
        List<String> ids = new ArrayList<>();

        for (JsonNode node : results)
            if (parseDouble(node.get("popularity").asText()) > LEAST_POPULARITY)
                ids.add(node.get("id").asText());

        return ids;
    }

    // JsonNode Mapper
    public JsonNode mapJsonNode(String path, String append, String query) throws JsonProcessingException {
        WebClient client = WebClient.builder()
                .baseUrl(TMDB_URL)
                .defaultHeader(HttpHeaders.AUTHORIZATION, TMDB_KEY)
                .build();

        String result = client.get()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .queryParam("append_to_response", append)
                        .queryParam("query", query)
                        .queryParam("language", LANGUAGE_KOREAN)
                        .queryParam("page", DEFAULT_PAGE)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return new ObjectMapper().readTree(result);
    }
}
