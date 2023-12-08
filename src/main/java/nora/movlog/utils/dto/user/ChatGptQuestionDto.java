package nora.movlog.utils.dto.user;

import lombok.Getter;

import java.io.Serializable;
import java.util.List;

@Getter
public class ChatGptQuestionDto implements Serializable {
    private String movieId;
    private List<String> keywords;
}
