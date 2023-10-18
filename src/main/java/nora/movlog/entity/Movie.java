package nora.movlog.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;

import java.time.LocalDateTime;
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
    @ManyToMany
    private Set<Genre>      genres;     // 장르
    @ManyToMany
    private Set<Director>   directors;  // 감독
    @ManyToMany
    private Set<Actor>      actors;     // 배우
}

