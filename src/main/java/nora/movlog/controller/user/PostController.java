package nora.movlog.controller.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nora.movlog.domain.user.Member;
import nora.movlog.service.user.CommentService;
import nora.movlog.service.user.ImageService;
import nora.movlog.service.user.MemberService;
import nora.movlog.service.user.PostService;
import nora.movlog.utils.MemberFinder;
import nora.movlog.utils.dto.user.CommentCreateRequestDto;
import nora.movlog.utils.dto.user.PostCreateRequestDto;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

import static nora.movlog.utils.constant.StringConstant.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(POST_URI)
@Controller
public class PostController {

    private final PostService postService;
    private final CommentService commentService;
    private final ImageService imageService;
    private final MemberService memberService;



    // 게시글 읽기
    @GetMapping("/{postId}")
    public String readPost(@PathVariable long postId,
                           @RequestParam(defaultValue = DEFAULT_SEARCH_PAGE) int page,
                           @RequestParam(defaultValue = DEFAULT_SEARCH_SIZE) int size,
                           Authentication auth,
                           Model model) {
        Member member = memberService.findByLoginId(MemberFinder.getLoginId(auth));

        model.addAttribute("loginMember", member);
        model.addAttribute("post", postService.findOne(postId));
        model.addAttribute("comments", commentService.findAllFromPost(postId, page, size));
        model.addAttribute("commentDto", CommentCreateRequestDto.builder().build());

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
        postService.write(dto, MemberFinder.getLoginId(auth));

        if (!movieId.isBlank())
            return "redirect:" + MOVIE_URI + "/" + movieId;

        return "redirect:" + SEARCH_URI;
    }


    // 게시글 삭제
    @PostMapping(ID_URI + "/delete")
    public String deletePost(@PathVariable(name = "id") long postId,
                             Authentication auth) throws IOException {
        Member member = memberService.findByLoginId(MemberFinder.getLoginId(auth));

        if (!postService.isWrittenFrom(member.getLoginId(), postId)) return null;

        postService.delete(postId);

        return "redirect:" + MEMBER_URI + member.getId();
    }


    // 게시글 신고
    @ResponseBody
    @PostMapping(ID_URI + "/")
    public void reportPost(@PathVariable(name = "id") long postId) {
        postService.report(postId);
    }


    // 이미지 다운로드
    @ResponseBody
    @GetMapping(ID_URI + IMAGE_URI)
    public ResponseEntity<UrlResource> downloadImage(@PathVariable(name = "id") long postId) {
        return imageService.download(postId);
    }
}
