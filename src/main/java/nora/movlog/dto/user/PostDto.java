package nora.movlog.dto.user;

import lombok.Builder;
import lombok.Data;
import nora.movlog.domain.user.Post;

import java.time.LocalDateTime;

@Builder
@Data
public class PostDto {
    /* Post */
    private Long id;
    private String body;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    /* Movie */
    private String movieId;

    /* User */
    private String userLoginId;
    private String userNickName;

    /* Like */
    private Integer likeCnt;

    public static PostDto of(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .body(post.getBody())
                .createdAt(post.getCreatedAt())
                .lastModifiedAt(post.getLastModifiedAt())
                .movieId(post.getMovie().getId())
                .userLoginId(post.getUser().getLoginId())
                .userNickName(post.getUser().getNickname())
                .likeCnt(post.getLikes().size())
                .build();
    }
}
