package nora.movlog.dto.user;

import lombok.Builder;
import lombok.Data;
import nora.movlog.domain.movie.Movie;
import nora.movlog.domain.user.Post;
import nora.movlog.domain.user.Member;

@Builder
@Data
public class PostCreateRequestDto {
    private String body;
    private Movie movie;

    public Post toEntity(Member member) {
        return Post.builder()
                .member(member)
                .body(body)
                .movie(movie)
                .build();
    }
}
