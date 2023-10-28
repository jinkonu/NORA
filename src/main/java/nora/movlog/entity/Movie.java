package nora.movlog.entity;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Slf4j
public class Movie {
    /* 데이터 관리 및 조회용 */
    @Id @GeneratedValue
    private Long            id;         // DB ID
    private String          kobisId;    // KOBIS API ID



    /* 영화 데이터 */
    private String          titleKo;    // 한국어 제목
    private String          titleEn;    // 영어 제목
    private String          prdtYear;   // 제작 연도
    private Long            showTime;   // 상영 시간
    private WatchGrade      watchGrade; // 상영 등급
    private String          nation;     // 개봉 국가
    @ManyToMany( cascade = CascadeType.ALL)
    private Set<Genre>      genre;      // 장르
    @ManyToMany ( cascade = CascadeType.ALL )
    private Set<Director>   directors;  // 감독
    @ManyToMany ( cascade = CascadeType.ALL )
    private Set<Actor>      actors;     // 배우



    /* 메서드 */
    // "영화 상세정보" 검색
    public static Movie createFromKobisMovieInfo(JsonNode jsonNode) {
        Movie movie = new Movie();

        movie.setKobisId(jsonNode.get("movieCd").textValue());
        movie.setTitleKo(jsonNode.get("movieNm").textValue());
        movie.setTitleEn(jsonNode.get("movieEnNm").textValue());
        movie.setPrdtYear(jsonNode.get("prdtYear").textValue());
        movie.setShowTime(Long.parseLong(jsonNode.get("showTm").textValue()));
        movie.setWatchGrade(watchGradeParser(jsonNode.get("audits")));
        movie.setGenre(genreParser(jsonNode.get("genres")));
        movie.setDirectors(directorParser(jsonNode.get("directors")));
        movie.setActors(actorParser(jsonNode.get("actors")));

        return movie;
    }

    // "영화목록" 검색
    public static Movie createFromKobisMovieList(JsonNode jsonNode) {
        Movie movie = new Movie();

        movie.setKobisId(jsonNode.get("movieCd").textValue());
        movie.setTitleKo(jsonNode.get("movieNm").textValue());
        movie.setTitleEn(jsonNode.get("movieNmEn").textValue());
        movie.setPrdtYear(jsonNode.get("prdtYear").textValue());

        return movie;

//        "movieCd": "20232909",
//                "movieNm": "펄프픽션",
//                "movieNmEn": "",
//                "prdtYear": "2023",
//                "openDt": "",
//                "typeNm": "장편",
//                "prdtStatNm": "기타",
//                "nationAlt": "한국",
//                "genreAlt": "드라마",
//                "repNationNm": "한국",
//                "repGenreNm": "드라마",
//                "directors": [],
//        "companys": []
    }


    private static WatchGrade watchGradeParser(JsonNode audits) {
        String grade = audits.get(audits.size() - 1).get("watchGradeNm").textValue();

        return switch (grade) {
            case "15세이상관람가"  -> WatchGrade.FIFTEEN;
            case "12세이상관람가"  -> WatchGrade.TWELVE;
            case "전체관람가"     -> WatchGrade.ALL;
            default            -> WatchGrade.ADULT;
        };
    }

    private static Set<Genre> genreParser(JsonNode genres) {
        HashSet<Genre> genreSet = new HashSet<>();

        for (JsonNode genre : genres) {
            genreSet.add(new Genre(genre.get("genreNm").textValue()));
        }

        return genreSet;
    }

    private static Set<Director> directorParser(JsonNode directors) {
        HashSet<Director> directorSet = new HashSet<>();

        for (JsonNode director : directors) {
            directorSet.add(new Director(director.get("peopleNm").textValue()));
        }

        return directorSet;
    }

    private static Set<Actor> actorParser(JsonNode actors) {
        HashSet<Actor> actorSet = new HashSet<>();

        for (JsonNode actor : actors) {
            actorSet.add(new Actor(actor.get("peopleNm").textValue()));
        }

        return actorSet;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "kobisId='" + kobisId + '\'' +
                ", titleKo='" + titleKo + '\'' +
                ", prdtYear='" + prdtYear + '\'' +
                ", showTime=" + showTime +
                ", watchGrade=" + watchGrade +
                ", nation='" + nation + '\'' +
                '}';
    }
}

