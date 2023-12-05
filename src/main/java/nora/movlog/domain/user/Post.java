package nora.movlog.domain.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nora.movlog.domain.movie.Movie;
import nora.movlog.utils.dto.user.PostEditDto;

import java.util.List;

import static nora.movlog.utils.constant.NumberConstant.BIGGER;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class Post extends BaseEntity implements Comparable<Post> {
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
    private int commentCnt;

    @OneToMany(mappedBy = "post", orphanRemoval = true)
    private List<Likes> likes;
    private int likeCnt;

    @OneToOne(fetch = FetchType.LAZY)
    private Image image;


    /* 메서드 */
    public void update(PostEditDto dto) {
        this.body = dto.getBody();
    }

    public void addComment() {
        ++commentCnt;
    }

    public void addLike() {
        ++likeCnt;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    @Override
    public int compareTo(Post other) {
        if (other == null)
            return BIGGER;

        return this.getCreatedAt().compareTo(other.getCreatedAt());
    }
}
