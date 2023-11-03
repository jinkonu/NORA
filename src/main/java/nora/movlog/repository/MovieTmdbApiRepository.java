package nora.movlog.repository;

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

        nodes.add(mapJsonNode(id, "", tmdbCreditsPath));    // basic info + credits info
        nodes.add(mapJsonNode(id, tmdbGradePath, ""));    // grade info

        return MovieTmdbDto.create(nodes);
    }

    // JsonNode Mapper
    public JsonNode mapJsonNode(String id, String path, String append) throws JsonProcessingException {
        WebClient client = WebClient.builder()
                .baseUrl(tmdbUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, tmdbKey)
                .build();

        String result = client.get()
                .uri(uriBuilder -> uriBuilder
                        .path(tmdbSearchByIdUrlPath + id + path)
                        .queryParam("append_to_response", append)
                        .queryParam("language", LANGUAGE_KOREAN)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return new ObjectMapper().readTree(result);
    }
}
