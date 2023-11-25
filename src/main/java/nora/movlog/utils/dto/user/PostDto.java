package nora.movlog.utils.dto.user;

import lombok.Builder;
import lombok.Data;
import nora.movlog.domain.user.Post;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static nora.movlog.domain.constant.NumberConstant.*;

@Builder
@Data
public class PostDto {
    /* Post */
    private Long id;
    private String body;
    private String createdAt;
    private String lastModifiedAt;

    /* Movie */
    private String movieId;
    private String movieTitle;

    /* Member */
    private long memberId;
    private String memberNickname;

    /* Like */
    private Integer likeCnt;

    /* Date */
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static PostDto of(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .body(post.getBody())
                .movieId(post.getMovie().getId())
                .movieTitle(post.getMovie().getTitleKo())
                .memberId(post.getMember().getId())
                .memberNickname(post.getMember().getNickname())
                .likeCnt(DEFAULT_LIKE_CNT)
                .createdAt(formatter.format(post.getCreatedAt()))
                .lastModifiedAt(formatter.format(post.getLastModifiedAt()))
                .build();
    }
}
