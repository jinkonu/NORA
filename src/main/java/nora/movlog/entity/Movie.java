package nora.movlog.entity;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Movie {
    /* 데이터 관리 및 조회용 */
    @Id
    private String          id;         // DB ID == API ID
    private String          kobisId;    // KOBIS API ID



    /* 영화 데이터 */
    private String          titleKo;    // 한국어 제목
    private String          titleEn;    // 영어 제목
    private Long            showTime;   // 상영 시간
    private String          prdtYear;   // 제작 연도
    private String          nation;     // 개봉 국가
    @ManyToMany( cascade = CascadeType.ALL)
    private Set<Genre>      genre;      // 장르
    @ManyToMany ( cascade = CascadeType.ALL )
    private Set<Director>   directors;  // 감독
    @ManyToMany ( cascade = CascadeType.ALL )
    private Set<Actor>      actors;     // 배우
    private WatchGrade      watchGrade; // 상영 등급



    /* 메서드 */
    // MovieJpaRepository 테스트를 위한 mock Movie 객체 생성
    public static Movie createMockMovie(String id, String titleKo) {
        Movie movie = new Movie();

        movie.setId(id);
        movie.setTitleKo(titleKo);

        return movie;
    }

    // "영화 상세정보" 검색
    public static Movie createFromKobisMovieInfo(JsonNode jsonNode) {
        Movie movie = new Movie();

        movie.setKobisId(jsonNode.get("movieCd").asText());
        movie.setKobisId(jsonNode.get("movieCd").asText("NONE"));
        movie.setTitleKo(jsonNode.get("movieNm").asText("NONE"));
        movie.setTitleEn(jsonNode.get("movieNmEn").asText("NONE"));

        if (jsonNode.get("showTm").asText().isEmpty())
            movie.setShowTime((long) 0);
        else
            movie.setShowTime(Long.parseLong(jsonNode.get("showTm").asText()));

        movie.setPrdtYear(jsonNode.get("prdtYear").asText("NONE"));

        // using parser
        movie.setNation(nationParser(jsonNode.get("nations")));
        movie.setGenre(genreParser(jsonNode.get("genres")));
        movie.setDirectors(directorParser(jsonNode.get("directors")));
        movie.setActors(actorParser(jsonNode.get("actors")));
        movie.setWatchGrade(watchGradeParser(jsonNode.get("audits")));

        return movie;
    }

    private static String nationParser(JsonNode nations) {
        if (!nations.isEmpty())
            return nations.get(0).get("nationNm").textValue();

        return "";
    }

    private static WatchGrade watchGradeParser(JsonNode audits) {
        if (!audits.isEmpty()) {
            String grade = audits.get(audits.size() - 1).get("watchGradeNm").textValue();

            return switch (grade) {
                case "15세이상관람가"  -> WatchGrade.FIFTEEN;
                case "12세이상관람가"  -> WatchGrade.TWELVE;
                case "전체관람가"     -> WatchGrade.ALL;
                default            -> WatchGrade.ADULT;
            };
        }

        return WatchGrade.NONE;
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

