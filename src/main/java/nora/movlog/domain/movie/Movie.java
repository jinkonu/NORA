package nora.movlog.domain.movie;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import nora.movlog.dto.movie.MovieTmdbDto;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class Movie {
    /* id from TMDB */
    @Id
    private String          id;         // TMDB ID == DB ID

    /* 영화 데이터 */
    private String          titleKo;    // 한국어 제목
    private String          titleEn;    // 영어 제목
    private String          runTime;   // 상영 시간
    private String          prdtYear;   // 제작 연도

    @ManyToMany( cascade = CascadeType.ALL)
    private Set<Nation>     nations;    // 제작 국가
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

    // TMDB API의 dto에서 Movie 객체로 변환
    public static Movie createFromTmdbDto(MovieTmdbDto dto) {
        return Movie.builder()
                .id(dto.getId())
                .titleKo(dto.getTitleKo())
                .titleEn(dto.getTitleEn())
                .runTime(dto.getRunTime())
                .prdtYear(dto.getPrdtYear())
                .watchGrade(dto.getWatchGrade())
                .build();
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id='" + id + '\'' +
                ", titleKo='" + titleKo + '\'' +
                ", prdtYear='" + prdtYear + '\'' +
                ", showTime=" + runTime +
                ", watchGrade=" + watchGrade +
                '}';
    }
}
