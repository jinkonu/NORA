package nora.movlog.utils.dto.user;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentEditDto {
    private String body;
}
