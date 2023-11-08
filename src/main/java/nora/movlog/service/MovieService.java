package nora.movlog.service;

import lombok.RequiredArgsConstructor;
import nora.movlog.domain.*;
import nora.movlog.dto.MovieTmdbDto;
import nora.movlog.repository.MovieJpaRepository;
import nora.movlog.repository.MovieTmdbApiRepository;
import nora.movlog.repository.interfaces.ActorRepository;
import nora.movlog.repository.interfaces.DirectorRepository;
import nora.movlog.repository.interfaces.GenreRepository;
import nora.movlog.repository.interfaces.NationRepository;
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

    private final NationRepository nationRepository;
    private final GenreRepository genreRepository;
    private final DirectorRepository directorRepository;
    private final ActorRepository actorRepository;

    // 현재까지 영화 제목 기반으로만 검색 -> 1차적으로 DB 검색 -> 없거나 부족하면 KOBIS API에 질의
    @Transactional( readOnly = true )
    public List<Movie> search(String searchDt) {
        List<Movie> dbMovieList = movieJpaRepository.findByName(searchDt);
        List<MovieTmdbDto> tmdbDtos = new ArrayList<>();

        if (dbMovieList.size() < MIN_SEARCH_LIST_SIZE) {
            try {
                tmdbDtos = movieTmdbApiRepository.findByQuery(searchDt);
            } catch (IOException e) { }
        }

        for (MovieTmdbDto dto : tmdbDtos)
            if (findOne(dto.getId()) == null) {
                Movie movie = createFromTmdbDto(dto);
                dbMovieList.add(movie);
                join(movie);
            }

        return dbMovieList;
    }

    // DB ID 기반 조회
    @Transactional( readOnly = true )
    public Movie findOne(String id) {
        return movieJpaRepository.findById(id);
    }

    // DB에 신규 영화 저장
    @Transactional( readOnly = false )
    public void join(Movie movie) {
        movieJpaRepository.save(movie);
    }
    
    // MovieTmdbDto -> Movie
    @Transactional( readOnly = false )
    public Movie createFromTmdbDto(MovieTmdbDto dto) {
        // 연관관계 없는 필드만 채움
        Movie movie = Movie.createFromTmdbDto(dto);
        
        // 연관관계 있는 필드 채워야 함
        movie.setNations(fillNations(dto.getNation()));
        movie.setGenre(fillGenres(dto.getGenres()));
        movie.setDirectors(fillDirectors(dto.getDirectors()));
        movie.setActors(fillActors(dto.getActors()));

        return movie;
    }

    // Nation 조회해서 채워줌
    @Transactional( readOnly = true )
    public Set<Nation> fillNations(Set<String> ids) {
        Set<Nation> nations = new HashSet<>();

        for (String id : ids) {
            Optional<Nation> nation = nationRepository.findById(id);
            if (nation.isPresent()) nations.add(nation.get());
            else {
                nationRepository.save(new Nation(id));
                nation = nationRepository.findById(id);
                if (nation.isPresent()) nations.add(nation.get());
            }
        }

        return nations;
    }

    // Genre 조회해서 채워줌
    @Transactional( readOnly = true )
    public Set<Genre> fillGenres(Set<String> ids) {
        Set<Genre> genres = new HashSet<>();

        for (String id : ids) {
            Optional<Genre> genre = genreRepository.findById(id);
            if (genre.isPresent()) genres.add(genre.get());
            else genreRepository.save(new Genre(id));
        }

        return genres;
    }

    // Director 조회해서 채워줌
    @Transactional( readOnly = true )
    public Set<Director> fillDirectors(Map<String, String> ids) {
        Set<Director> directors = new HashSet<>();

        for (String id : ids.keySet()) {
            Optional<Director> director = directorRepository.findById(id);
            if (director.isPresent()) directors.add(director.get());
            else directorRepository.save(new Director(id, ids.get(id)));
        }

        return directors;
    }

    // Actor 조회해서 채워줌
    @Transactional( readOnly = true )
    public Set<Actor> fillActors(Map<String, String> ids) {
        Set<Actor> actors = new HashSet<>();

        for (String id : ids.keySet()) {
            Optional<Actor> actor = actorRepository.findById(id);
            if (actor.isPresent()) actors.add(actor.get());
            else actorRepository.save(new Actor(id, ids.get(id)));
        }

        return actors;
    }
}
