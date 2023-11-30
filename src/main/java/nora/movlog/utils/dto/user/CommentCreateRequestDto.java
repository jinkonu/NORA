package nora.movlog.utils.dto.user;

import lombok.Builder;
import lombok.Data;
import nora.movlog.domain.user.Comment;
import nora.movlog.domain.user.Member;
import nora.movlog.domain.user.Post;

@Builder
@Data
public class CommentCreateRequestDto {
    private String body;

    public Comment toEntity(Member member, Post post) {
        return Comment.builder()
                .body(body)
                .member(member)
                .post(post)
                .build();
    }
}
