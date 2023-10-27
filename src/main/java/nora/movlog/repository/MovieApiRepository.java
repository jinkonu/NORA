package nora.movlog.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import nora.movlog.entity.Movie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;

/*
외부 API에 쿼리 날리는 리포지토리 클래스
포함 : Kobis
 */

@Repository
public class MovieApiRepository {
    static String kobisKey = "4c9099b4c7f44c7a34192082ead54dbe";
    static String kobisUrl = "http://www.kobis.or.kr";
    static String kobisSearchByIdUrlPath = "/kobisopenapi/webservice/rest/movie/searchMovieInfo.json";
    static String kboisSearchByNameUrlPath = "/kobisopenapi/webservice/rest/movie/searchMovieList.json";


    // kobis api 주소에 http 요청을 날리는 메서드
    public Movie findByKobisString(String searchDt) throws JsonProcessingException {
        // 아래 부분은 향후 Utility와 같은 이름의 클래스로 따로 빼야 할듯
        WebClient client = WebClient.builder()
                .baseUrl(kobisUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        String result = client.get()
                .uri(uriBuilder -> uriBuilder
                        .path(kboisSearchByNameUrlPath)
                        .queryParam("key", kobisKey)
                        .queryParam("movieNm", searchDt)    // 지금은 영화 제목에만 넣고 있음
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        JsonNode movieDtoNode = new ObjectMapper().readTree(result).get("movieListResult");

        return Movie
    }

    // kobis api 주소에 http 요청을 날리는 메서드
    public Movie findByKobisId(String movieCd) throws JsonProcessingException {
        // 아래 부분은 향후 Utility와 같은 이름의 클래스로 따로 빼야 할듯
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

        return Movie.createFromKobisJsonNode(movieDtoNode);
    }
}
