package nora.movlog.service.movie;

import lombok.RequiredArgsConstructor;
import nora.movlog.domain.movie.Movie;
import nora.movlog.dto.movie.MovieTmdbDto;
import nora.movlog.repository.movie.MovieJpaRepository;
import nora.movlog.repository.movie.MovieTmdbApiRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

import static nora.movlog.constant.NumberConstant.*;

/*
영화 서비스
 */

@RequiredArgsConstructor
@Service
public class MovieService {
    private final MovieJpaRepository movieJpaRepository;
    private final MovieTmdbApiRepository movieTmdbApiRepository;

    private final NationService nationService;
    private final GenreService genreService;
    private final DirectorService directorService;
    private final ActorService actorService;



    /* CREATE */

    // 현재까지 영화 제목 기반으로만 검색 -> 1차적으로 DB 검색 -> 없거나 부족하면 KOBIS API에 질의
    @Transactional
    public List<Movie> search(String searchDt) throws IOException {
        List<Movie> dbMovieList = movieJpaRepository.findByName(searchDt);
        List<MovieTmdbDto> tmdbDtos = new ArrayList<>();

        if (dbMovieList.size() < MIN_SEARCH_LIST_SIZE)
            tmdbDtos = movieTmdbApiRepository.findByQuery(searchDt);

        for (MovieTmdbDto dto : tmdbDtos)
            if (findOne(dto.getId()) == null) {
                Movie movie = createFromTmdbDto(dto);
                dbMovieList.add(movie);
                join(movie);
            }

        return dbMovieList;
    }

    // DB에 신규 영화 저장
    @Transactional
    public void join(Movie movie) {
        movieJpaRepository.save(movie);
    }

    // MovieTmdbDto로부터 Movie 객체 생성
    @Transactional
    public Movie createFromTmdbDto(MovieTmdbDto dto) {
        // 연관관계 없는 필드만 채움
        Movie movie = Movie.createFromTmdbDto(dto);

        // 연관관계 있는 필드 채움
        movie.setNations(nationService.fillNations(dto.getNation()));
        movie.setGenre(genreService.fillGenres(dto.getGenres()));
        movie.setDirectors(directorService.fillDirectors(dto.getDirectors()));
        movie.setActors(actorService.fillActors(dto.getActors()));

        return movie;
    }



    /* RETRIEVE */

    // DB ID 기반 조회
    @Transactional( readOnly = true )
    public Movie findOne(String id) {
        return movieJpaRepository.findById(id);
    }
}
