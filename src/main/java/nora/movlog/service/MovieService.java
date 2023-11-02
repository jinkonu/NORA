package nora.movlog.service;

import lombok.RequiredArgsConstructor;
import nora.movlog.constant.NumberConstant;
import nora.movlog.domain.Movie;
import nora.movlog.repository.MovieKobisApiRepository;
import nora.movlog.repository.MovieJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
영화 서비스
 */



@RequiredArgsConstructor
@Service
public class MovieService {
    private final MovieKobisApiRepository movieKobisApiRepository;
    private final MovieJpaRepository movieJpaRepository;

    // KOBIS "movieCd"를 기반으로 KOBIS API에 질의해서 검색
    public Movie findByKobisId(String kobisId) {
        Movie movie = null;

        try {
            movie = movieKobisApiRepository.findByKobisId(kobisId);
        } catch (IOException e) {
            // 키 값 기반 검색이기 때문에 예외가 발생할 경우가 거의 없을듯
        }

        return movie;
    }

    // 현재까지 영화 제목 기반으로만 검색 -> 1차적으로 DB 검색 -> 없거나 부족하면 KOBIS API에 질의
    @Transactional( readOnly = true )
    public List<Movie> search(String searchDt) {
        List<Movie> dbMovieList = movieJpaRepository.findByName(searchDt);
        List<Movie> apiMovieList = new ArrayList<>();

        if (dbMovieList.size() < NumberConstant.MIN_SEARCH_LIST_SIZE) {
            try {
                apiMovieList = movieKobisApiRepository.findByKobisString(searchDt);
                join(apiMovieList);
            } catch (IOException e) {
                // 잘못된 이름을 검색할 경우 예외 발생 대신 검색결과가 없으므로 발생할 경우가 거의 없을듯
            }
        }

        dbMovieList.addAll(apiMovieList);
        return dbMovieList;
    }

    // DB ID 기반 조회
    @Transactional( readOnly = true )
    public Movie findOne(String id) {
        return movieJpaRepository.findById(id);
    }

    // DB에 신규 영화 저장
    @Transactional( readOnly = false )
    public void join(List<Movie> movieList) {
        for (Movie movie : movieList) {
            if (movieJpaRepository.findById(movie.getId()) == null)
                movieJpaRepository.save(movie);
        }
    }
}
