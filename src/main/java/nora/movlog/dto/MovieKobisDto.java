package nora.movlog.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

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

