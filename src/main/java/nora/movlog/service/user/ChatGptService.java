package nora.movlog.service.user;

import lombok.RequiredArgsConstructor;
import nora.movlog.repository.movie.interfaces.MovieRepository;
import nora.movlog.utils.dto.user.ChatGptQuestionDto;
import nora.movlog.utils.dto.user.ChatGptRequestDto;
import nora.movlog.utils.dto.user.ChatGptResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static nora.movlog.utils.constant.NumberConstant.*;
import static nora.movlog.utils.constant.StringConstant.*;

@Service
@RequiredArgsConstructor
public class ChatGptService {

    private final MovieRepository movieRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${chatgpt.api-key}")
    private String api_key;

    public HttpEntity<ChatGptRequestDto> buildHttpEntity(ChatGptRequestDto requestDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(MEDIA_TYPE));
        headers.add(AUTHORIZATION, BEARER + api_key);
        return new HttpEntity<>(requestDto, headers);
    }

    public ChatGptResponseDto getResponse(HttpEntity<ChatGptRequestDto> chatGptRequestDtoHttpEntity) {
        ResponseEntity<ChatGptResponseDto> responseEntity = restTemplate.postForEntity(
                OPENAI_URL,
                chatGptRequestDtoHttpEntity,
                ChatGptResponseDto.class);

        return responseEntity.getBody();
    }

    public ChatGptResponseDto askQuestion(ChatGptQuestionDto questionDto) {
        String prompt = "영화 '" + movieRepository.findById(questionDto.getMovieId()).get() + "에 대한 감상평을 ";
        List<String> keywords = questionDto.getKeywords();
        int keywordSize = keywords.size();
        for (int i = 0; i < keywordSize; i++) {
            prompt += "'" + keywords.get(i) + "'";
            if (i != keywordSize - 1) prompt += ", ";
        }
        prompt += "의 키워드를 넣어서 200자 이내로 작성해줘.";

        return this.getResponse(
                this.buildHttpEntity(
                        new ChatGptRequestDto(
                                MODEL,
                                prompt,
                                MAX_TOKEN,
                                TEMPERATURE,
                                TOP_P
                        )
                )
        );
    }
}
