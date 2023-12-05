package nora.movlog.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nora.movlog.domain.user.Member;
import nora.movlog.service.auth.AuthService;
import nora.movlog.service.user.MemberService;
import nora.movlog.service.user.PostService;
import nora.movlog.utils.MemberFinder;
import nora.movlog.utils.dto.user.MemberJoinRequestDto;
import nora.movlog.utils.dto.user.MemberLoginRequestDto;
import nora.movlog.utils.validators.MemberValidator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;

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
    public void joinPage(@Valid @ModelAttribute MemberJoinRequestDto dto,
                           BindingResult bindingResult,
                           HttpServletResponse response) throws IOException {
        if (memberValidator.validateJoin(dto, bindingResult).hasErrors()) {
            PrintWriter out = response.getWriter();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html; charset=UTF-8");
            out.println("<script> alert('이미 가입된 이메일입니다.'); location.href='" + JOIN_URI + "' </script>");
            out.close();
        }
        else {
            memberService.join(dto);
            PrintWriter out = response.getWriter();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html; charset=UTF-8");
            out.println("<script> alert('가입되었습니다.'); location.href='" + LOGIN_URI + "' </script>");
            out.close();
        }
    }

    // 로그인
    @GetMapping(LOGIN_URI)
    public String loginPage(Model model) {
        model.addAttribute("memberLoginRequest", new MemberLoginRequestDto());
        return "loginPage";
    }
}
