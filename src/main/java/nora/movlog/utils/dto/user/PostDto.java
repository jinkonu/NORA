package nora.movlog.utils.dto.user;

import lombok.Builder;
import lombok.Data;
import nora.movlog.domain.user.Image;
import nora.movlog.domain.user.Post;
import org.springframework.web.multipart.MultipartFile;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;

import static nora.movlog.utils.constant.NumberConstant.*;

@Builder
@Data
public class PostDto implements Comparable<PostDto> {
    private static final Comparator<String> COMPARATOR = String.CASE_INSENSITIVE_ORDER.reversed();

    /* Post */
    private long id;
    private String body;
    private String createdAt;
    private String lastModifiedAt;

    /* Movie */
    private String movieId;
    private String movieTitle;

    /* Member */
    private String memberLoginId;
    private String memberNickname;

    /* Like */
    private int likeCnt;

    /* Comment */
    private int commentCnt;

    /* Image */
    private Image nowImage;
    private MultipartFile newImage;

    /* Date */
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static PostDto of(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .body(post.getBody())
                .movieId(post.getMovie().getId())
                .movieTitle(post.getMovie().getTitleKo())
                .memberLoginId(post.getMember().getLoginId())
                .memberNickname(post.getMember().getNickname())
                .likeCnt(post.getLikeCnt())
                .commentCnt(post.getCommentCnt())
                .createdAt(formatter.format(post.getCreatedAt()))
                .lastModifiedAt(formatter.format(post.getLastModifiedAt()))
                .nowImage(post.getImage())
                .build();
    }

    @Override
    public int compareTo(PostDto dto) {
        return COMPARATOR.compare(this.createdAt, dto.createdAt);
    }
}
