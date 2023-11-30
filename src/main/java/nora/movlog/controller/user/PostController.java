package nora.movlog.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nora.movlog.service.user.PostService;
import nora.movlog.utils.MemberFinder;
import nora.movlog.utils.dto.user.PostCreateRequestDto;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static nora.movlog.utils.constant.StringConstant.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(POST_URI)
@Controller
public class PostController {

    private final PostService postService;



    // 게시글 읽기
    @GetMapping("/{postId}")
    public String readPost(@PathVariable long postId,
                           Model model) {
        model.addAttribute("post", postService.findOne(postId));

        return "postPage";
    }


    // 게시글 쓰기
    @GetMapping
    public String writePost(@RequestParam String movieId,
                            Model model) {
        model.addAttribute("postDto", PostCreateRequestDto.builder()
                .movieId(movieId)
                .build());

        return "writePost";
    }

    @PostMapping
    public String writePost(@ModelAttribute PostCreateRequestDto dto,
                            @RequestParam String movieId,
                            Authentication auth) {
        postService.write(dto, MemberFinder.getUsernameFrom(auth));

        if (!movieId.isBlank())
            return "redirect:" + MOVIE_URI + movieId;

        return "redirect:" + SEARCH_URI;
    }
}
