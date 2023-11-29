package nora.movlog.service.movie;

import lombok.RequiredArgsConstructor;
import nora.movlog.domain.movie.Movie;
import nora.movlog.utils.dto.movie.MovieTmdbDto;
import nora.movlog.repository.movie.MovieTmdbApiRepository;
import nora.movlog.repository.movie.interfaces.MovieRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static nora.movlog.utils.constant.NumberConstant.*;

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



    // DB에 신규 영화 저장
    @Transactional
    public void join(Movie movie) {
        movieRepository.save(movie);
    }

    @Transactional
    public void joinAll(List<Movie> movies) {
        movieRepository.saveAll(movies);
    }

    @Transactional
    public List<Movie> findAndJoinFromTmdb(String query, int dbMoviesSize) {
        List<Movie> movies = new ArrayList<>();

        for (MovieTmdbDto dto : movieTmdbApiRepository.findByQuery(query, dbMoviesSize))
            if (findOne(dto.getId()) == null)
                movies.add(createFromTmdbDto(dto));

        joinAll(movies);
        return movies;
    }

    // MovieTmdbDto로부터 Movie 객체 생성
    @Transactional
    public Movie createFromTmdbDto(MovieTmdbDto dto) {
        return MovieTmdbDto.toEntity(dto);
    }



    /* READ */

    // 문자열 기반 검색
    //  -> 1차적으로 DB 검색
    //  -> 없거나 부족하면 findFromTmdb()
    @Transactional
    public List<Movie> search(String query, int pageNumber, int pageSize) {
        List<Movie> movies = new ArrayList<>(movieRepository.findAllByTitleKoContains(query, PageRequest.of(pageNumber, pageSize))
                .stream().toList());

        if (movies.size() < MIN_SEARCH_LIST_SIZE)
            movies.addAll(findAndJoinFromTmdb(query, movies.size()));

        movies.sort(Comparator.comparingDouble(Movie::getPopularity).reversed());
        return movies;
    }

    // DB ID 기반 조회
    @Transactional( readOnly = true )
    public Movie findOne(String id) {
        return movieRepository.findById(id).orElse(null);
    }
}
