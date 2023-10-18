package nora.movlog.service;

import lombok.RequiredArgsConstructor;
import nora.movlog.entity.Movie;
import nora.movlog.repository.MovieApiRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;

/*
영화 페이지 서비스
 */

@RequiredArgsConstructor
@Service
public class MovieService {
    private final MovieApiRepository movieApiRepository;

    // KOBIS "movieCd"를 기반으로 KOBIS API에 질의해서 검색
    public Movie findByKobisId(String kobisId) {
        Movie movie = null;

        try {
            movie = movieApiRepository.findByKobis(kobisId);
        } catch (IOException e) {
            // 키 값 기반 검색이기 때문에 예외가 발생할 경우가 거의 없을듯
        }

        return movie;
    }

    public HashMap<String, String> search(String searchDt) {
        return new HashMap<>();
    }
}
