package nora.movlog.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nora.movlog.domain.user.Member;
import nora.movlog.service.user.MemberService;
import nora.movlog.service.user.PostService;
import nora.movlog.utils.MemberFinder;
import nora.movlog.utils.dto.user.MemberJoinRequestDto;
import nora.movlog.utils.dto.user.MemberLoginRequestDto;
import nora.movlog.utils.dto.user.VerificationRequestDto;
import nora.movlog.utils.validators.MemberValidator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static nora.movlog.utils.constant.StringConstant.*;

@Slf4j
@RequiredArgsConstructor
@Controller
public class HomeController {

    private final MemberService memberService;
    private final PostService postService;
    private final MemberValidator memberValidator;



    // 홈
    @GetMapping(value={NOTHING_URI, HOME_URI})
    public String home(Authentication auth,
                       Model model) {
        Member member = memberService.findByLoginId(MemberFinder.getUsernameFrom(auth));

        model.addAttribute("loginMember", member);
        model.addAttribute("posts", postService.findHomePosts(member.getLoginId()));

        return "homePage";
    }


    // 회원가입
    @GetMapping(JOIN_URI)
    public String joinPage(Model model) {
        model.addAttribute("memberJoinRequest", new MemberJoinRequestDto());

        return "joinPage";
    }

    @PostMapping(JOIN_URI)
    public String joinPage(@Valid @ModelAttribute MemberJoinRequestDto dto,
                           BindingResult bindingResult) {
        if (memberValidator.validateJoin(dto, bindingResult).hasErrors())
            return "redirect:" + JOIN_URI;

        memberService.join(dto);
        memberService.sendCodeToEmail(dto.getLoginId());
        return "redirect:" + JOIN_URI + VERIFY_URI + "/" + dto.getLoginId();
    }


    // 이메일 인증
    @GetMapping(JOIN_URI + VERIFY_URI + "/{loginId}")
    public String verifyPage(Authentication auth,
                             Model model) {
        if(auth==null || !auth.isAuthenticated()) {
            model.addAttribute("verifyRequest", new VerificationRequestDto());
            return "verifyPage";
        }
        else return "redirect:/search";
    }

    @PostMapping(JOIN_URI + VERIFY_URI + "/{loginId}")
    public String verifyPage(@PathVariable String loginId,
                             @Valid @ModelAttribute VerificationRequestDto dto) {
        if (memberService.verifiedCode(loginId, dto.getVerifyCode())) {
            memberService.findByLoginId(loginId).setVerified();
            return "redirect:/login";
        }
        else return "redirect: " + JOIN_URI + VERIFY_URI + loginId; // 수정해야 함
    }


    // 로그인
    @GetMapping(LOGIN_URI)
    public String loginPage(Model model) {
        model.addAttribute("memberLoginRequest", new MemberLoginRequestDto());

        return "loginPage";
    }
}
