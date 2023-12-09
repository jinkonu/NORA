package nora.movlog.controller.user;

import lombok.RequiredArgsConstructor;
import nora.movlog.service.user.ChatGptService;
import nora.movlog.utils.dto.user.ChatGptQuestionDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static nora.movlog.utils.constant.StringConstant.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(POST_URI + CHATGPT_URI)
public class ChatGptController {

    private final ChatGptService chatGptService;

    @PostMapping
    public String sendQuestion(@RequestBody ChatGptQuestionDto requestDto) {
        return chatGptService.askQuestion(requestDto).getChoices().get(0).getText();
    }
}