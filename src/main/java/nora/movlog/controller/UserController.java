package nora.movlog.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nora.movlog.dto.user.UserJoinRequestDto;
import nora.movlog.service.user.PostService;
import nora.movlog.service.user.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
@Controller
public class UserController {
    private final UserService userService;
    private final PostService postService;



    /* 회원 */

    // 회원가입 페이지
    @GetMapping("/join")
    public String joinPage(Model model) {
        model.addAttribute("userJoinRequest", new UserJoinRequestDto());

        return "joinPage";
    }

    @PostMapping("/join")
    public String joinPage(@Valid @ModelAttribute UserJoinRequestDto dto, BindingResult bindingResult, Model model) {
        if (userService.validateJoin(dto, bindingResult).hasErrors())
            return "/user/join";

        userService.join(dto);

//        model.addAttribute("message", "회원가입에 성공했습니다!\n로그인 후 사용 가능합니다!");
//        model.addAttribute("nextUrl", "/users/login");
        return "redirect:/";
    }


    // 로그인 페이지
    @GetMapping("/login")
    public String loginPage() {
        return "loginPage";
    }



    /* 게시물 */

    // 게시물 생성
    @GetMapping("/{id}/post")
    public String postPage() {
        return "writePost";
    }

    @PostMapping("/{id}/post")
    public String writePost(@PathVariable long id, @RequestParam("post") String post) {
        postService.write(post, id);

        return "redirect:/user" + id;
    }
}
