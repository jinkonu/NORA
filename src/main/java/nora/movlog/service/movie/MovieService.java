package nora.movlog.service.movie;

import lombok.RequiredArgsConstructor;
import nora.movlog.domain.movie.Movie;
import nora.movlog.dto.movie.MovieTmdbDto;
import nora.movlog.repository.movie.MovieTmdbApiRepository;
import nora.movlog.repository.movie.interfaces.MovieRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static nora.movlog.domain.constant.NumberConstant.*;

/*
영화 서비스
 */

@RequiredArgsConstructor
@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private final MovieTmdbApiRepository movieTmdbApiRepository;

    private final NationService nationService;
    private final GenreService genreService;
    private final DirectorService directorService;
    private final ActorService actorService;



    /* CREATE */
    /* READ */

    // 현재까지 영화 제목 기반으로만 검색 -> 1차적으로 DB 검색 -> 없거나 부족하면 TMDB API에 질의해서 채움
    @Transactional
    public List<Movie> search(String searchDt) {
        List<Movie> dbMovieList = new ArrayList<>(movieRepository.findAllByTitleKoContains(searchDt));

        if (dbMovieList.size() < MIN_SEARCH_LIST_SIZE) {
            List<MovieTmdbDto> tmdbDtos = movieTmdbApiRepository.findByQuery(searchDt);

            for (MovieTmdbDto dto : tmdbDtos)
                if (findOne(dto.getId()) == null) {
                    Movie movie = createFromTmdbDto(dto);
                    dbMovieList.add(movie);
                    join(movie);
                }
        }

        return dbMovieList;
    }

    // DB에 신규 영화 저장
    @Transactional
    public void join(Movie movie) {
        movieRepository.save(movie);
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



    /* READ */

    // DB ID 기반 조회
    @Transactional( readOnly = true )
    public Movie findOne(String id) {
        return movieRepository.findById(id).orElse(null);
    }
}
