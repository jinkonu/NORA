package nora.movlog.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nora.movlog.dto.MovieKobisDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;

/*
외부 API에 쿼리 날리는 리포지토리 클래스
포함 : Kobis
 */

@Repository
public class MovieApiRepository {
    public MovieKobisDto findByKobis(String movieCd) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        String key = "4c9099b4c7f44c7a34192082ead54dbe";
        String url = "http://www.kobis.or.kr";
        String urlPath = "/kobisopenapi/webservice/rest/movie/searchMovieInfo.json";

        // 아래 부분은 향후 Utility와 같은 이름의 클래스로 따로 빼야 할듯
        // kobis api 주소에 http 요청을 날리는 메서드
        WebClient client = WebClient.builder()
                .baseUrl(url)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        String result = client.get()
                .uri(uriBuilder -> uriBuilder
                        .path(urlPath)
                        .queryParam("key", key)
                        .queryParam("movieCd", movieCd)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return mapper.readValue(result, MovieKobisDto.class);
    }
}
