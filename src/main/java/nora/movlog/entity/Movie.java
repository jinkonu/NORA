package nora.movlog.entity;

import jakarta.persistence.*;
import lombok.Data;
import nora.movlog.dto.MovieKobisDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
@Entity
public class Movie {
    /* 데이터 관리 및 조회용 */
    @Id @GeneratedValue
    private Long            id;         // DB ID

    private Long            tmbdId;     // TMBD API ID
    private String          kobisId;    // KOBIS API ID



    /* 영화 데이터 */
    private String          titleKo;    // 한국어 제목
    private String          titleEn;    // 영어 제목
    private LocalDateTime   openDate;   // 개봉 날짜
    private Long            showTime;   // 상영 시간
    private WatchGrade      watchGrade; // 상영 등급
    private String          nation;     // 개봉 국가
    private String          genre;      // 장르
    @ManyToMany ( cascade = CascadeType.ALL )
    private Set<Director>   directors;  // 감독
    @ManyToMany ( cascade = CascadeType.ALL )
    private Set<Actor>      actors;     // 배우

    public static Movie fromKobistoEntity(MovieKobisDto movieDto) {
        Movie movie = new Movie();

        movie.setKobisId(movieDto.getMovieCd());
        movie.setTitleKo(movieDto.getMovieNm());
        movie.setTitleEn(movieDto.getMovieNmEn());
        movie.setOpenDate(LocalDateTime.parse(movieDto.getOpenDt(), DateTimeFormatter.BASIC_ISO_DATE));
        movie.setShowTime(Long.parseLong(movieDto.getShowTm()));
        movie.setWatchGrade(watchGradeParser(movieDto.getAudits()));
        movie.setGenre(genreParser(movieDto.getGenres()));
        movie.setDirectors(directorParser(movieDto.getDirectors()));
        movie.setActors(actorParser(movieDto.getActors()));

        return movie;
    }

    private static Set<Actor> actorParser(List<Map<String, String>> actors) {
        Set<Actor> actorSet = new HashSet<>();

        for (Map<String, String> act : actors) {
            String name = act.get("peopleNm");
            Actor actor = new Actor(name);
        }

        return actorSet;
    }

    private static Set<Director> directorParser(List<Map<String, String>> directors) {
        Set<Director> directorSet = new HashSet<>();

        for (Map<String, String> dir : directors) {
            String name = dir.get("peopleNm");
            Director director = new Director(name);
        }

        return directorSet;
    }

    private static String genreParser(List<Map<String, String>> genres) {
        return genres.get(0).get("genreNm");
    }

    private static WatchGrade watchGradeParser(List<Map<String, String>> audits) {
        String grade = audits.get(0).get("watchGradeNm");

        return switch (grade) {
            case "15세이상관람가"  -> WatchGrade.FIFTEEN;
            case "12세이상관람가"  -> WatchGrade.TWELVE;
            case "전체관람가"     -> WatchGrade.ALL;
            default            -> WatchGrade.ADULT;
        };
    }
}

