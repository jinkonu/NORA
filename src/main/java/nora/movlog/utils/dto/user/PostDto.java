package nora.movlog.utils.dto.user;

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
    private long memberId;
    private String memberNickname;

    /* Like */
    private Integer likeCnt;

    public static PostDto of(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .body(post.getBody())
                .movieId(post.getMovie().getId())
                .memberId(post.getMember().getId())
                .memberNickname(post.getMember().getNickname())
                .likeCnt(DEFAULT_LIKE_CNT)
                .createdAt(post.getCreatedAt())
                .lastModifiedAt(post.getLastModifiedAt())
                .build();
    }
}
