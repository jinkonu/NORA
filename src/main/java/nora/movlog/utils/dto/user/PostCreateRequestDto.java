package nora.movlog.utils.dto.user;

import lombok.Builder;
import lombok.Data;
import nora.movlog.domain.movie.Movie;
import nora.movlog.domain.user.Post;
import nora.movlog.domain.user.Member;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Data
public class PostCreateRequestDto {
    private String body;
    private String movieId;
    private MultipartFile image;

    public Post toEntity(Member member, Movie movie) {
        return Post.builder()
                .member(member)
                .body(body)
                .movie(movie)
                .build();
    }
}
