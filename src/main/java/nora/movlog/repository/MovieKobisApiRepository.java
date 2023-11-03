package nora.movlog.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nora.movlog.domain.Movie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

import static nora.movlog.constant.NumberConstant.*;
import static nora.movlog.domain.constant.StringConstant.*;

/*
KOBIS API에 쿼리 날리는 리포지토리
 */

@Repository
public class MovieKobisApiRepository {
    /* kobis api 주소에 http 요청을 날리는 메서드 */
    // 문자열 기반으로 검색
    public List<Movie> findByKobisString(String searchDt) throws JsonProcessingException {
        List<Movie> movieList = new ArrayList<>();

        // 아래 부분은 향후 Utility와 같은 이름의 클래스로 따로 빼야 할듯
        WebClient client = WebClient.builder()
                .baseUrl(kobisUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        String result = client.get()
                .uri(uriBuilder -> uriBuilder
                        .path(kobisSearchByNameUrlPath)
                        .queryParam("key", kobisKey)
                        .queryParam("movieNm", searchDt)    // 지금은 영화 제목에만 넣고 있음
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        JsonNode movieMetaNode = new ObjectMapper().readTree(result).get("movieListResult");
        JsonNode movieDtoNode = movieMetaNode.get("movieList");
        int movieCnt = Math.min(movieMetaNode.get("totCnt").asInt(), MAX_SEARCH_LIST_SIZE);

        for (int i = 0; i < movieCnt; i++)
            movieList.add(findByKobisId(movieDtoNode.get(i).get("movieCd").textValue()));

        return movieList;
    }

    // movieCd 기반으로 검색
    public Movie findByKobisId(String movieCd) throws JsonProcessingException {
        WebClient client = WebClient.builder()
                .baseUrl(kobisUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        String result = client.get()
                .uri(uriBuilder -> uriBuilder
                        .path(kobisSearchByIdUrlPath)
                        .queryParam("key", kobisKey)
                        .queryParam("movieCd", movieCd)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        JsonNode movieDtoNode = new ObjectMapper().readTree(result).get("movieInfoResult").get("movieInfo");

        return new Movie();
    }


}
