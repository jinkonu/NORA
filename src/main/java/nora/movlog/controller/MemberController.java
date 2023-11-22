package nora.movlog.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nora.movlog.utils.dto.user.MemberJoinRequestDto;
import nora.movlog.service.user.PostService;
import nora.movlog.service.user.MemberService;
import nora.movlog.utils.validators.MemberValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/member")
@Slf4j
@Controller
public class MemberController {
    private final MemberService memberService;
    private final PostService postService;

    private final MemberValidator memberValidator;



    /* 회원 */

    // 회원가입 페이지
    @GetMapping("/join")
    public String joinPage(Model model) {
        model.addAttribute("memberJoinRequest", new MemberJoinRequestDto());

        return "joinPage";
    }

    @PostMapping("/join")
    public String joinPage(@Valid @ModelAttribute MemberJoinRequestDto dto,
                           BindingResult bindingResult,
                           Model model) {
        // 회원가입에 필요한 폼을 양식에 맞게 채우지 못했을 때
        if (memberValidator.validateJoin(dto, bindingResult).hasErrors())
            return "/member/join";

        memberService.join(dto);

        return "redirect:/";
    }


    // 로그인 페이지
    @GetMapping("/login")
    public String loginPage() {
        return "loginPage";
    }


    // 프로필 페이지
    @GetMapping("/{id}")
    public String profilePage(@PathVariable long id,
                              Model model) {
        model.addAttribute("member", memberService.profile(id));

        return "userPage";
    }


    /* 게시물 */

    // 게시물 생성
    @GetMapping("/{id}/post")
    public String postPage(@PathVariable long id,
                           Model model) {
        model.addAttribute("id", id);

        return "writePost";
    }

    @PostMapping("/{id}/post")
    public String writePost(@PathVariable long id,
                            @RequestParam("post") String body,
                            @RequestParam("movieId") String movieId) {
        postService.write(body, movieId, id);

        return "redirect:/member/" + id;
    }
}
