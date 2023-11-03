package nora.movlog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import nora.movlog.domain.*;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class MovieKobisDto {
    private String movieCd;                     // KOBIS API ID
    private String movieNm;                     // 한국어 제목
    private String movieNmEn;                   // 영어 제목
    private String openDt;                      // 개봉 날짜
    private String showTm;                      // 상영 시간
    private List<Map<String, String>> audits;   // 상영 등급
    private List<Map<String, String>> nations;  // 개봉 국가
    private List<Map<String, String>> genres;   // 장르
    private List<Map<String, String>> directors;// 감독
    private List<Map<String, String>> actors;   // 배우

    @SuppressWarnings("unchecked")
    @JsonProperty("movieInfo")
    public void readJson(Map<String, Object> movieInfo) {
        movieCd = (String) movieInfo.get("movieCd");
        movieNm = (String) movieInfo.get("movieNm");
        openDt = (String) movieInfo.get("openDt");
        showTm = (String) movieInfo.get("showTm");
        audits = (List<Map<String, String>>) movieInfo.get("audits");
        nations = (List<Map<String, String>>) movieInfo.get("nations");
        genres = (List<Map<String, String>>) movieInfo.get("genres");
        directors = (List<Map<String, String>>) movieInfo.get("directors");
        actors = (List<Map<String, String>>) movieInfo.get("actors");
    }

    // "영화 상세정보" 검색
    public static Movie createFromKobisMovieInfo(JsonNode jsonNode) {
        Movie movie = new Movie();

        movie.setId(jsonNode.get("movieCd").asText());
        movie.setTitleKo(jsonNode.get("movieNm").asText("NONE"));
        movie.setTitleEn(jsonNode.get("movieNmEn").asText("NONE"));
        movie.setRunTime(jsonNode.get("showTm").asText("0"));
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
}

//public class MovieInfo {
//    private String movieCd;
//    private String movieNm;
//    private String movieNmEn;
//    private String showTm;
//    private String prdtYear;
//    private String openDt;
//    private String prdtStatNm;
//    private String typeNm;
//    private List<Nation> nations;
//    private List<Genre> genres;
//    private List<Director> directors;
//    private List<Actor> actors;
//    private List<ShowType> showTypes;
//    private List<Company> companys;
//    private List<Audit> audits;
//
//    // 게터와 세터 메서드
//}

