package nora.movlog.utils.dto.user;

import lombok.Builder;
import lombok.Data;
import nora.movlog.domain.user.Comment;

import java.time.LocalDateTime;

@Builder
@Data
public class CommentDto {
    /* Comment */
    private Long id;
    private String body;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    /* Member */
    private String memberLoginId;
    private String memberNickname;
    private String memberImageUrl;

    /* Post */
    private long postId;

    public static CommentDto of(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .body(comment.getBody())
                .memberLoginId(comment.getMember().getLoginId())
                .memberNickname(comment.getMember().getNickname())
                .memberImageUrl(comment.getMember().getImageUrl())
                .postId(comment.getPost().getId())
                .build();
    }
}
