package nora.movlog.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nora.movlog.utils.dto.user.MemberJoinRequestDto;
import nora.movlog.utils.dto.user.MemberLoginRequestDto;
import nora.movlog.service.user.PostService;
import nora.movlog.service.user.MemberService;
import org.springframework.security.core.Authentication;
import nora.movlog.utils.validators.MemberValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static nora.movlog.domain.constant.StringConstant.*;

@RequiredArgsConstructor
@RequestMapping("/")
@Slf4j
@Controller
public class MemberController {
    private final MemberService memberService;
    private final PostService postService;
    private final MemberValidator memberValidator;

    @GetMapping(value={"", "/"})
    public String home(Model model, Authentication auth) {

        return "home";
    }

    /* 회원 */

    // 회원가입 페이지
    @GetMapping("/join")
    public String joinPage(Model model) {
        model.addAttribute("memberJoinRequest", new MemberJoinRequestDto());
        return "join_form";
    }

    @PostMapping("/join")
    public String joinPage(@Valid @ModelAttribute MemberJoinRequestDto dto,
                           BindingResult bindingResult,
                           Model model) {
        // 회원가입에 필요한 폼을 양식에 맞게 채우지 못했을 때
        if (memberValidator.validateJoin(dto, bindingResult).hasErrors())
            return "redirect:/join";

        memberService.join(dto);

        return "redirect:/login";
    }


    // 로그인 페이지
    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("memberLoginRequest", new MemberLoginRequestDto());
        return "login_form";
    }


    // 프로필 페이지
    @GetMapping("/member/{id}")
    public String profilePage(@PathVariable long id,
                              @RequestParam(defaultValue = DEFAULT_SEARCH_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_SEARCH_SIZE) int size,
                              Model model) {
        model.addAttribute("member", memberService.profile(id));
        model.addAttribute("posts", postService.findAllFromMember(id, page, size));

        return "userPage";
    }


    /* 게시물 */

    // 게시물 생성
    @GetMapping("/member/{id}/post")
    public String postPage(@PathVariable long id,
                           Model model) {
        model.addAttribute("id", id);

        return "writePost";
    }

    @PostMapping("/member/{id}/post")
    public String writePost(@PathVariable long id,
                            @RequestParam("post") String body,
                            @RequestParam("movieId") String movieId) {
        postService.write(body, movieId, id);

        return "redirect:/member/" + id;
    }
}
