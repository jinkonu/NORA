package nora.movlog.controller.auth;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nora.movlog.service.auth.AuthService;
import nora.movlog.service.user.MemberService;
import nora.movlog.utils.MemberFinder;
import nora.movlog.utils.dto.auth.VerificationRequestDto;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;

import static nora.movlog.utils.constant.StringConstant.*;

@Slf4j
@RequiredArgsConstructor
@Controller
public class AuthController {

    private final AuthService authService;
    private final MemberService memberService;

    // 이메일 인증
    @RequestMapping(CHECK_VERIFY_URI)
    public String checkVerify(Authentication auth) {
        String authId = MemberFinder.getUsernameFrom(auth);
        if (memberService.findByLoginId(authId).getMemberAuth().equals(AUTH_VERIFIED)) return "redirect:" + SEARCH_URI;
        else {
            authService.sendCodeToEmail(authId);
            return "redirect:" + VERIFY_URI;
        }
    }

    @GetMapping(VERIFY_URI)
    public String verifyPage(Authentication auth,
                             Model model) {
        String loginId = MemberFinder.getUsernameFrom(auth);
        model.addAttribute("loginId", loginId);
        model.addAttribute("verifyRequest", new VerificationRequestDto());
        return "verifyPage";
    }

    @PostMapping(VERIFY_URI)
    public void verifyPage(Authentication auth,
                             @Valid @ModelAttribute VerificationRequestDto dto,
                             HttpServletResponse response) throws IOException {
        String loginId = MemberFinder.getUsernameFrom(auth);
        if (authService.verifiedCode(loginId, dto.getVerifyCode())) {
            authService.setVerified(loginId, auth);
            PrintWriter out = response.getWriter();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html; charset=UTF-8");
            out.println("<script> alert('인증되었습니다.'); location.href='/search' </script>");
            out.close();
        }
        else {
            PrintWriter out = response.getWriter();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/html; charset=UTF-8");
            out.println("<script> alert('인증번호가 일치하지 않습니다.'); location.href='/verify' </script>");
            out.close();
        }
    }

    @GetMapping(VERIFY_URI + RESEND_URI)
    public String resendCode(Authentication auth) {
        String loginId = MemberFinder.getUsernameFrom(auth);
        authService.sendCodeToEmail(loginId);
        return "redirect:" + VERIFY_URI;
    }
}
