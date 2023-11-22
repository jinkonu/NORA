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
    private long memberId;
    private String memberNickname;

    /* Post */
    private long postId;

    public static CommentDto of(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .body(comment.getBody())
                .memberId(comment.getId())
                .memberNickname(comment.getMember().getNickname())
                .postId(comment.getPost().getId())
                .build();
    }
}
