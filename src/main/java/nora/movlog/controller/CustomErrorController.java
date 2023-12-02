package nora.movlog.controller;

import lombok.RequiredArgsConstructor;
import nora.movlog.service.user.MemberService;
import nora.movlog.utils.MemberFinder;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import static nora.movlog.utils.constant.StringConstant.*;

@RequiredArgsConstructor
@Controller
public class CustomErrorController implements ErrorController {

    private final MemberService memberService;

    @RequestMapping("/error")
    public String error(Authentication auth) {
        if(auth==null) {
            return "redirect:" + LOGIN_URI;
        }
        else {
            String authId = MemberFinder.getUsernameFrom(auth);
            if (memberService.findByLoginId(authId).getMemberAuth().equals(AUTH_UNVERIFIED))
                return "redirect:" + VERIFY_URI + "/" + authId;
            else return "redirect:" + SEARCH_URI;
        }
    }
}
