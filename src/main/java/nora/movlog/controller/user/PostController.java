package nora.movlog.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nora.movlog.service.user.CommentService;
import nora.movlog.service.user.ImageService;
import nora.movlog.service.user.PostService;
import nora.movlog.utils.MemberFinder;
import nora.movlog.utils.dto.user.PostCreateRequestDto;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;

import static nora.movlog.utils.FileUtility.getFullPath;
import static nora.movlog.utils.constant.StringConstant.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(POST_URI)
@Controller
public class PostController {

    private final PostService postService;
    private final CommentService commentService;
    private final ImageService imageService;



    // 게시글 읽기
    @GetMapping("/{postId}")
    public String readPost(@PathVariable long postId,
                           @RequestParam(defaultValue = DEFAULT_SEARCH_PAGE) int page,
                           @RequestParam(defaultValue = DEFAULT_SEARCH_SIZE) int size,
                           Model model) {
        model.addAttribute("post", postService.findOne(postId));
        model.addAttribute("comments", commentService.findAllFromPost(postId, page, size));

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
                            Authentication auth) throws IOException {
        postService.write(dto, MemberFinder.getUsernameFrom(auth));

        if (!movieId.isBlank())
            return "redirect:" + MOVIE_URI + movieId;

        return "redirect:" + SEARCH_URI;
    }


    // 이미지 읽기
    @ResponseBody
    @GetMapping(IMAGE_URI + "/{fileName}")
    public Resource readImage(@PathVariable String fileName) throws MalformedURLException {
        return new UrlResource("file:" + getFullPath(fileName));
    }


    // 이미지 다운로드
    @GetMapping(IMAGE_URI + "/download/{postId}")
    public ResponseEntity<UrlResource> downloadImage(@PathVariable long postId) throws MalformedURLException {
        return imageService.download(postId);
    }
}
