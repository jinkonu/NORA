package nora.movlog.repository.movie;

/*
TMDB API에 쿼리 날리는 리포지토리
 */

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nora.movlog.dto.MovieTmdbDto;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

import static nora.movlog.domain.constant.StringConstant.*;

@Repository
public class MovieTmdbApiRepository {
    // id 기반으로 TMDB API에 질의해서 dto 제공
    public MovieTmdbDto findById(String id) throws JsonProcessingException {
        List<JsonNode> nodes = new ArrayList<>();

        nodes.add(mapJsonNode(tmdbSearchByIdUrlPath + id, tmdbCreditsPath, ""));                    // basic info + credits info
        nodes.add(mapJsonNode(tmdbSearchByIdUrlPath + id + "/" + tmdbGradePath, "", ""));    // grade info

        return MovieTmdbDto.create(nodes);
    }

    // 문자열 기반으로 TMDB API에 질의해서 dto 제공
    public List<MovieTmdbDto> findByQuery(String query) throws JsonProcessingException {
        List<MovieTmdbDto> dtos = new ArrayList<>();
        JsonNode result = mapJsonNode(tmdbSearchByQueryUrlPath, "", query).get("results");

        for (JsonNode node : result)
            dtos.add(findById(node.get("id").asText()));

        return dtos;
    }

    // JsonNode Mapper
    public JsonNode mapJsonNode(String path, String append, String query) throws JsonProcessingException {
        WebClient client = WebClient.builder()
                .baseUrl(tmdbUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, tmdbKey)
                .build();

        String result = client.get()
                .uri(uriBuilder -> uriBuilder
                        .path(path)
                        .queryParam("append_to_response", append)
                        .queryParam("query", query)
                        .queryParam("language", LANGUAGE_KOREAN)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return new ObjectMapper().readTree(result);
    }
}
