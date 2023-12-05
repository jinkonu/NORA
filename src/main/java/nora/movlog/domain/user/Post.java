package nora.movlog.domain.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nora.movlog.domain.movie.Movie;
import nora.movlog.utils.dto.user.PostDto;
import nora.movlog.utils.dto.user.PostEditDto;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static nora.movlog.utils.constant.NumberConstant.BIGGER;

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
    private int commentCnt;

    @OneToMany(mappedBy = "post", orphanRemoval = true)
    private List<Likes> likes;
    private int likeCnt;

    @OneToOne(fetch = FetchType.LAZY)
    private Image image;
    private final String rootPath = System.getProperty("user.dir");
    private final String fileDirection = rootPath + "/src/main/resources/static/img/";


    /* 메서드 */
    public void update(PostDto dto) {
        this.body = dto.getBody();
    }

    public void changeComment(int commentCnt) {
        this.commentCnt = commentCnt;
    }

    public void changeLike(int likeCnt) {
        this.likeCnt = likeCnt;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
