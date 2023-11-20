package nora.movlog.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nora.movlog.service.user.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
@Controller
public class UserController {
    private final PostService postService;

    /* 게시물 작성 */

    @GetMapping("/{id}/post")
    public String writePost() {
        return "writePost";
    }

    @PostMapping("/{id}/post")
    public String registerPost(@PathVariable long id, @RequestParam("post") String post) {
        postService.writePost(post, id);

        return "redirect:/user" + id;
    }
}
