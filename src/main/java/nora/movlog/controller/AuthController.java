package nora.movlog.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nora.movlog.service.user.AuthService;
import nora.movlog.service.user.MemberService;
import nora.movlog.utils.MemberFinder;
import nora.movlog.utils.dto.user.VerificationRequestDto;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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
            return "redirect:" + VERIFY_URI + "/" + authId;
        }
    }

    @GetMapping(VERIFY_URI + "/{loginId}")
    public String verifyPage(@PathVariable String loginId,
                             Authentication auth,
                             Model model) {
        if(auth==null || !auth.isAuthenticated()) {
            model.addAttribute("loginId", loginId);
            model.addAttribute("verifyRequest", new VerificationRequestDto());
            return "verifyPage";
        }
        else return "redirect:" + SEARCH_URI;
    }

    @PostMapping(JOIN_URI + VERIFY_URI + "/{loginId}")
    public String verifyPage(@PathVariable String loginId,
                             @Valid @ModelAttribute VerificationRequestDto dto) {
        if (authService.verifiedCode(loginId, dto.getVerifyCode())) return "redirect:" + LOGIN_URI;
        else return "redirect: " + JOIN_URI + VERIFY_URI + loginId;
    }
}
