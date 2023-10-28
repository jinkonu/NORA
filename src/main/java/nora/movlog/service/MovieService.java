package nora.movlog.service;

import lombok.RequiredArgsConstructor;
import nora.movlog.entity.Movie;
import nora.movlog.repository.MovieApiRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
            movie = movieApiRepository.findByKobisId(kobisId);
        } catch (IOException e) {
            // 키 값 기반 검색이기 때문에 예외가 발생할 경우가 거의 없을듯
        }

        return movie;
    }

    public List<Movie> search(String searchDt) {
        List<Movie> movieList = new ArrayList<>();

        try {
            movieList = movieApiRepository.findByKobisString(searchDt);
        } catch (IOException e) {
            // 잘못된 이름을 검색할 경우 예외 발생 대신 검색결과가 없으므로 발생할 경우가 거의 없을듯
        }

        return movieList;
    }
}
