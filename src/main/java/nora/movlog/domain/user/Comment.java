package nora.movlog.domain.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nora.movlog.utils.dto.user.CommentEditDto;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Entity
public class Comment {
    /* DB id */
    @Id
    @GeneratedValue
    private Long id;

    /* 댓글 데이터 */
    private String body;

    /* 연관관계 */
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;


    /* 메서드 */
    public void update(CommentEditDto dto) {
        this.body = dto.getBody();
    }
}
