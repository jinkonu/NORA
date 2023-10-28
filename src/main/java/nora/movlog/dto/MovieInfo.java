package nora.movlog.dto;

import lombok.Data;

@Data
public class MovieInfo {
    private String movieCd;
    private String movieNm;

    // 생성자, 기타 필드, 게터, 세터 등 필요한 메서드

    public String getMovieCd() {
        return movieCd;
    }

    public void setMovieCd(String movieCd) {
        this.movieCd = movieCd;
    }

    public String getMovieNm() {
        return movieNm;
    }

    public void setMovieNm(String movieNm) {
        this.movieNm = movieNm;
    }
}

