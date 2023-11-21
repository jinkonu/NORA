package nora.movlog.dto.user;

import lombok.Builder;
import lombok.Data;
import nora.movlog.domain.user.Post;

import java.time.LocalDateTime;

import static nora.movlog.domain.constant.NumberConstant.*;

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

    /* Member */
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
                .userLoginId(post.getMember().getLoginId())
                .userNickName(post.getMember().getNickname())
                .likeCnt(DEFAULT_LIKE_CNT)
                .build();
    }
}
