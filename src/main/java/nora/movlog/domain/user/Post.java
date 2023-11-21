package nora.movlog.domain.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nora.movlog.domain.movie.Movie;
import nora.movlog.dto.user.PostEditDto;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class Post extends BaseEntity {
    /* DB id */
    @Id @GeneratedValue
    private Long id;

    /* 포스트 데이터 */
    private String body;

    /* 연관관계 */
    @ManyToOne(fetch = FetchType.LAZY)
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @OneToMany(mappedBy = "post", orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "post", orphanRemoval = true)
    private List<Likes> likes;



    /* 메서드 */
    public void update(PostEditDto dto) {
        this.body = dto.getBody();
    }
}
