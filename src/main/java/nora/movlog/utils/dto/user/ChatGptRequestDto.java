package nora.movlog.utils.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class ChatGptRequestDto implements Serializable {

    private String model;
    private String prompt;
    @JsonProperty("max_tokens")
    private int maxTokens;
    private double temperature;
    @JsonProperty("top_p")
    private Double topP;

    @Builder
    public ChatGptRequestDto(String model, String prompt,
                             int maxTokens, double temperature,
                             double topP) {
        this.model = model;
        this.prompt = prompt;
        this.maxTokens = maxTokens;
        this.temperature = temperature;
        this.topP = topP;
    }
}