package nora.movlog.controller.auth;

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
    public String verifyPage(Authentication auth,
                             @Valid @ModelAttribute VerificationRequestDto dto) {
        String loginId = MemberFinder.getUsernameFrom(auth);
        if (authService.verifiedCode(loginId, dto.getVerifyCode())) {
            authService.setVerified(loginId, auth);
            return "redirect:" + SEARCH_URI;
        }
        else return "redirect:" + VERIFY_URI;
    }

    @GetMapping(VERIFY_URI + RESEND_URI)
    public String resendCode(Authentication auth) {
        String loginId = MemberFinder.getUsernameFrom(auth);
        authService.sendCodeToEmail(loginId);
        return "redirect:" + VERIFY_URI;
    }
}
