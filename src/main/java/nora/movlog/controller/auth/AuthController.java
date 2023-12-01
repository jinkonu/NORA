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
            return "redirect:" + VERIFY_URI + "/" + authId;
        }
    }

    @GetMapping(VERIFY_URI + "/{loginId}")
    public String verifyPage(@PathVariable String loginId,
                             Authentication auth,
                             Model model) {
        String authId = MemberFinder.getUsernameFrom(auth);
        if (memberService.findByLoginId(authId).getMemberAuth().equals(AUTH_VERIFIED)) { // 인증된 유저가 접근 시도 시 검색 페이지로
            return "redirect:" + SEARCH_URI;
        }
        else { // 미인증된 유저는 인증 페이지로
            if (!authId.equals(loginId)) { // loginId가 자신의 아이디와 일치하지 않을 경우 자신의 인증 페이지로
                return "redirect:" + VERIFY_URI + "/" + authId;
            }
            else { // 인증되지 않은 유저에 대한 정상적인 인증 처리
                model.addAttribute("loginId", loginId);
                model.addAttribute("verifyRequest", new VerificationRequestDto());
                return "verifyPage";
            }
        }
    }

    @PostMapping(VERIFY_URI + "/{loginId}")
    public String verifyPage(@PathVariable String loginId,
                             Authentication auth,
                             @Valid @ModelAttribute VerificationRequestDto dto) {
        if (authService.verifiedCode(loginId, dto.getVerifyCode())) {
            authService.setVerified(loginId, auth);
            return "redirect:" + SEARCH_URI;
        }
        else return "redirect:" + VERIFY_URI + "/" + loginId;
    }
}
