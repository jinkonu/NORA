package nora.movlog.controller.user;

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

import static nora.movlog.utils.constant.StringConstant.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/member")
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
                           BindingResult bindingResult) {
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
                              @RequestParam(defaultValue = DEFAULT_SEARCH_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_SEARCH_SIZE) int size,
                              Model model) {
        model.addAttribute("member", memberService.profile(id));
        model.addAttribute("posts", postService.findAllFromMember(id, page, size));

        return "userPage";
    }
}
