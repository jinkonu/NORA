package nora.movlog.dto.user;

import lombok.Data;
import nora.movlog.domain.user.Post;
import nora.movlog.domain.user.User;

@Data
public class PostCreateRequestDto {
    private String body;

    public Post toEntity(User user) {
        return Post.builder()
                .user(user)
                .body(body)
                .build();
    }
}
