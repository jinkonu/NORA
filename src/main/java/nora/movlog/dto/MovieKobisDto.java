package nora.movlog.dto;

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
}
