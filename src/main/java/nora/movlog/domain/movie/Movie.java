package nora.movlog.domain.movie;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class Movie {
    /* DB id from TMDB */
    @Id
    private String          id;         // TMDB ID == DB ID

    /* 영화 데이터 */
    private String          titleKo;    // 한국어 제목
    private String          titleEn;    // 영어 제목
    private double          popularity; // 유명도
    private String          runTime;    // 상영 시간
    private String          prdtYear;   // 제작 연도

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Nation>     nations;    // 제작 국가
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Genre>      genres;      // 장르
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Director>   directors;  // 감독
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Actor>      actors;     // 배우

    private WatchGrade      watchGrade; // 상영 등급



    /* 메서드 */
}
